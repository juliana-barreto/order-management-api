package com.juliana_barreto.ecommerce.modules.order;

import com.juliana_barreto.ecommerce.modules.order_item.OrderItem;
import com.juliana_barreto.ecommerce.modules.order_item.OrderItemDTO;
import com.juliana_barreto.ecommerce.modules.product.Product;
import com.juliana_barreto.ecommerce.modules.product.ProductRepository;
import com.juliana_barreto.ecommerce.modules.user.User;
import com.juliana_barreto.ecommerce.modules.user.UserRepository;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  public OrderService(OrderRepository orderRepository, UserRepository userRepository,
      ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public List<OrderDTO> findAll() {
    List<Order> entities = orderRepository.findAllWithRelations();
    List<OrderDTO> dtos = new ArrayList<>();
    for (Order entity : entities) {
      dtos.add(new OrderDTO(entity));
    }
    return dtos;
  }

  @Transactional(readOnly = true)
  public OrderDTO findById(Long id) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    return new OrderDTO(entity);
  }

  @Transactional
  public OrderDTO create(OrderDTO dto) {
    Order entity = new Order();

    // Fetch and set client
    User client = userRepository.findById(dto.getClient().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Client not found."));
    entity.setClient(client);

    // Process Items, linking and ensuring real price from DB
    if (dto.getItems() != null && !dto.getItems().isEmpty()) {
      for (OrderItemDTO itemDto : dto.getItems()) {
        Long productId = itemDto.getProductId();
        // Fetch the real product to get the official price
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(itemDto.getQuantity());
        item.setUnitPrice(product.getPrice());
        item.setOrder(entity);
        entity.getItems().add(item);
      }
    }

    // Total calculation is handled by @PrePersist in the Entity
    entity = orderRepository.save(entity);
    return new OrderDTO(entity);
  }

  @Transactional
  public OrderDTO update(Long id, OrderDTO dto) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

    if (dto.getStatus() != null) {
      entity.setStatus(dto.getStatus());
    }

    entity = orderRepository.save(entity);
    return new OrderDTO(entity);
  }

  @Transactional
  public OrderDTO cancel(Long id) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

    // Business Rule: Cannot cancel if already shipped or delivered
    if (entity.getStatus() == OrderStatus.SHIPPED || entity.getStatus() == OrderStatus.DELIVERED) {
      throw new IllegalStateException(
          "Order cannot be canceled because it has already been shipped or delivered.");
    }

    // Business Rule: Cannot cancel if already canceled
    if (entity.getStatus() == OrderStatus.CANCELED) {
      throw new IllegalStateException("Order is already canceled.");
    }

    entity.setStatus(OrderStatus.CANCELED);
    entity = orderRepository.save(entity);
    return new OrderDTO(entity);
  }
}

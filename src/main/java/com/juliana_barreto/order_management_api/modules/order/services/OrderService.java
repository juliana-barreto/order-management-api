package com.juliana_barreto.order_management_api.modules.order;

import com.juliana_barreto.order_management_api.modules.order_item.OrderItem;
import com.juliana_barreto.order_management_api.modules.order_item.OrderItemRequest;
import com.juliana_barreto.order_management_api.modules.order_item.OrderItemResponse;
import com.juliana_barreto.order_management_api.modules.product.Product;
import com.juliana_barreto.order_management_api.modules.product.ProductRepository;
import com.juliana_barreto.order_management_api.modules.user.User;
import com.juliana_barreto.order_management_api.modules.user.UserRepository;
import com.juliana_barreto.order_management_api.modules.user.UserResponse;
import com.juliana_barreto.order_management_api.shared.exceptions.ResourceNotFoundException;
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
  public List<OrderResponse> findAll() {
    List<Order> entities = orderRepository.findAllWithRelations();
    List<OrderResponse> responses = new ArrayList<>();
    for (Order entity : entities) {
      responses.add(mapToResponse(entity));
    }
    return responses;
  }

  @Transactional(readOnly = true)
  public OrderResponse findById(Long id) {
    Order entity = orderRepository.findByIdWithRelations(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    return mapToResponse(entity);
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> findClientHistory(Long clientId) {
    // Fetch the specific user's order history
    List<Order> entities = orderRepository.findOrderHistoryByClientId(clientId);
    List<OrderResponse> responses = new ArrayList<>();

    for (Order entity : entities) {
      responses.add(mapToResponse(entity));
    }
    return responses;
  }

  @Transactional
  public OrderResponse create(OrderRequest request) {
    Order entity = new Order();

    // Fetch and set client
    User client = userRepository.findById(request.clientId())
        .orElseThrow(() -> new ResourceNotFoundException("Client not found."));
    entity.setClient(client);

    // Process Items, linking and ensuring real price from DB
    if (request.items() != null && !request.items().isEmpty()) {
      for (OrderItemRequest itemReq : request.items()) {
        Long productId = itemReq.productId();
        // Fetch the real product to get the official price
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(itemReq.quantity());
        item.setUnitPrice(product.getPrice()); // Ensure price is taken from the product
        item.setOrder(entity);
        entity.getItems().add(item);
      }
    }

    // Total calculation is handled by @PrePersist in the Entity
    entity = orderRepository.save(entity);
    return mapToResponse(entity);
  }

  @Transactional
  public OrderResponse markAsPaid(Long id) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

    entity.pay();
    entity = orderRepository.save(entity);
    return mapToResponse(entity);
  }

  @Transactional
  public OrderResponse shipOrder(Long id) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

    entity.ship();
    entity = orderRepository.save(entity);
    return mapToResponse(entity);
  }

  @Transactional
  public OrderResponse deliverOrder(Long id) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

    entity.deliver();
    entity = orderRepository.save(entity);
    return mapToResponse(entity);
  }

  @Transactional
  public OrderResponse cancel(Long id) {
    Order entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

    entity.cancel();
    entity = orderRepository.save(entity);
    return mapToResponse(entity);
  }

  private OrderResponse mapToResponse(Order entity) {
    List<OrderItemResponse> itemResponses = new ArrayList<>();
    for (OrderItem item : entity.getItems()) {
      itemResponses.add(new OrderItemResponse(item));
    }

    return new OrderResponse(
        entity.getId(),
        entity.getMoment(),
        entity.getStatus(),
        new UserResponse(entity.getClient()),
        itemResponses,
        entity.getOrderTotal()
    );
  }
}

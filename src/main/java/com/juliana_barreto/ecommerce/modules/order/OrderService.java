package com.juliana_barreto.ecommerce.modules.order;

import com.juliana_barreto.ecommerce.modules.order_item.OrderItem;
import com.juliana_barreto.ecommerce.modules.product.Product;
import com.juliana_barreto.ecommerce.modules.product.ProductRepository;
import com.juliana_barreto.ecommerce.modules.user.User;
import com.juliana_barreto.ecommerce.modules.user.UserRepository;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
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
  public List<Order> findAll() {
    return orderRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Order findById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
  }

  @Transactional
  public Order create(Order order) {
    // Validate and fetch Client
    if (order.getClient() == null || order.getClient().getId() == null) {
      throw new IllegalArgumentException("The order must be associated with an existing user.");
    }
    User client = userRepository.findById(order.getClient().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Client not found."));
    order.setClient(client);

    // Process Items, linking and ensuring real price from DB
    var items = order.getItems();
    if (items != null) {
      for (OrderItem item : items) {
        var productId = item.getProduct().getId();
        // Fetch the real product to get the official price
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        item.setProduct(product);
        item.setUnitPrice(product.getPrice());
        item.setOrder(order);
      }
    }

    // Total calculation is handled by @PrePersist in the Entity
    return orderRepository.save(order);
  }

  @Transactional
  public Order update(Long id, Order updatedData) {
    Order existingOrder = findById(id);

    if (updatedData.getStatus() != null) {
      existingOrder.setStatus(updatedData.getStatus());
    }

    return orderRepository.save(existingOrder);
  }

  @Transactional
  public Order cancel(Long id) {
    Order order = findById(id);

    // Business Rule: Cannot cancel if already shipped or delivered
    if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
      throw new IllegalStateException(
          "Order cannot be canceled because it has already been shipped or delivered.");
    }

    // Business Rule: Cannot cancel if already canceled
    if (order.getStatus() == OrderStatus.CANCELED) {
      throw new IllegalStateException("Order is already canceled.");
    }

    order.setStatus(OrderStatus.CANCELED);
    return orderRepository.save(order);
  }
}

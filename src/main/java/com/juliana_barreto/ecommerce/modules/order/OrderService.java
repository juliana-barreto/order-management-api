package com.juliana_barreto.ecommerce.modules.order;

import com.juliana_barreto.ecommerce.modules.user.User;
import com.juliana_barreto.ecommerce.modules.user.UserRepository;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
  }

  public List<Order> findAll() {
    return orderRepository.findAll();
  }

  public Order findById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
  }

  public Order create(Order order) {
    // Basic validation
    if (order.getClient() == null || order.getClient().getId() == null) {
      throw new IllegalArgumentException("The order must be associated with an existing user.");
    }

    // Fetch the real user from the database to ensure consistency
    User client = userRepository.findById(order.getClient().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Order user not found."));
    order.setClient(client);

    // Associate items with the order and calculate total
    if (order.getItems() != null) {
      for (OrderItem item : order.getItems()) {
        item.setOrder(order);
      }
    }

    order.calculateTotal(); // Your entity method
    return orderRepository.save(order);
  }

  public Order update(Long id, Order updatedData) {
    Order existingOrder = findById(id);

    if (updatedData.getStatus() != null) {
      existingOrder.setStatus(updatedData.getStatus());
    }

    return orderRepository.save(existingOrder);
  }

  public void delete(Long id) {
    if (!orderRepository.existsById(id)) {
      throw new ResourceNotFoundException("Order not found.");
    }
    orderRepository.deleteById(id);
  }
}

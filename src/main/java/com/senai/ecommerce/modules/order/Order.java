package com.senai.ecommerce.modules.order;

import com.senai.ecommerce.modules.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal orderTotal;

  @Column(nullable = false)
  private LocalDateTime orderDate = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status = OrderStatus.AWAITING_PAYMENT;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  public void addItem(OrderItem item) {
    this.items.add(item);
    item.setOrder(this);
    calculateTotal();
  }

  public void removeItem(OrderItem item) {
    this.items.remove(item);
    item.setOrder(null);
    calculateTotal();
  }

  public void calculateTotal() {
    var total = BigDecimal.ZERO;
    for (OrderItem item : this.items) {
      var quantity = BigDecimal.valueOf(item.getQuantity());
      BigDecimal itemTotal = item.getUnitPrice().multiply(quantity);
      total = total.add(itemTotal);
    }
    this.orderTotal = total;
  }

}

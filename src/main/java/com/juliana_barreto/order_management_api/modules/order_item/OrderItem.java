package com.juliana_barreto.order_management_api.modules.order_item;

import com.juliana_barreto.order_management_api.modules.order.Order;
import com.juliana_barreto.order_management_api.modules.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_order_item")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include
  @EmbeddedId
  @Setter(AccessLevel.NONE)
  private OrderItemPK id = new OrderItemPK();

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @Builder
  public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice) {
    this.id.setOrder(order);
    this.id.setProduct(product);
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public Order getOrder() {
    return id.getOrder();
  }

  public void setOrder(Order order) {
    id.setOrder(order);
  }

  public Product getProduct() {
    return id.getProduct();
  }

  public void setProduct(Product product) {
    id.setProduct(product);
  }

  public BigDecimal getSubTotal() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
}

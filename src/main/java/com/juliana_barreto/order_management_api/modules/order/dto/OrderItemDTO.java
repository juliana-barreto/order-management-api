package com.juliana_barreto.order_management_api.modules.order.dto;

import com.juliana_barreto.order_management_api.modules.order.entities.OrderItem;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemDTO implements Serializable {

  @NotNull(message = "Product ID is mandatory.")
  private Long productId;

  @NotNull(message = "Product name is mandatory.")
  private String productName;
  private String productImgUrl;

  @NotNull(message = "Quantity is mandatory.")
  @Positive(message = "Quantity must be greater than zero.")
  private Integer quantity;

  private BigDecimal unitPrice;
  private BigDecimal subTotal;

  public OrderItemDTO(OrderItem entity) {
    this.productId = entity.getProduct().getId();
    this.productName = entity.getProduct().getName();
    this.productImgUrl = entity.getProduct().getImgUrl();
    this.quantity = entity.getQuantity();
    this.unitPrice = entity.getUnitPrice();
    this.subTotal = entity.getSubTotal();
  }
}
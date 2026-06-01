package com.juliana_barreto.order_management_api.modules.order.dto;

import com.juliana_barreto.order_management_api.modules.order.entities.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
    Long productId,
    String productName,
    String productImgUrl,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subTotal
) {

  public OrderItemResponse(OrderItem entity) {
    this(
        entity.getProduct().getId(),
        entity.getProduct().getName(),
        entity.getProduct().getImgUrl(),
        entity.getQuantity(),
        entity.getUnitPrice(),
        entity.getSubTotal()
    );
  }
}

package com.juliana_barreto.order_management_api.modules.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderStatus {
  AWAITING_PAYMENT,
  PAID,
  SHIPPED,
  DELIVERED,
  CANCELED;
}
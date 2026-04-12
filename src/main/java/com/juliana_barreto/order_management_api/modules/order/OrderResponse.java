package com.juliana_barreto.order_management_api.modules.order;

import com.juliana_barreto.order_management_api.modules.order_item.OrderItemResponse;
import com.juliana_barreto.order_management_api.modules.user.UserResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long id,
    Instant moment,
    OrderStatus status,
    UserResponse client,
    List<OrderItemResponse> items,
    BigDecimal total
) {

}

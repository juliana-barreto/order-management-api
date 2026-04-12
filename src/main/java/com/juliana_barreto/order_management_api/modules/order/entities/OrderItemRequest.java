package com.juliana_barreto.order_management_api.modules.order_item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(

    @NotNull(message = "Product ID is mandatory.")
    Long productId,
    @NotNull(message = "Quantity is mandatory.")
    @Positive(message = "Quantity must be greater than zero.")
    Integer quantity

) {

}



package com.juliana_barreto.order_management_api.modules.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(

    @NotNull(message = "Client is mandatory.")
    Long clientId,

    @Valid
    @NotEmpty(message = "Order must have at least one item.")
    List<OrderItemRequest> items
) {

}

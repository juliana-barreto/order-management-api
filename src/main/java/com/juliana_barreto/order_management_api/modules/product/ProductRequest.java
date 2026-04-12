package com.juliana_barreto.order_management_api.modules.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

public record ProductRequest(

    @NotBlank(message = "Product name is mandatory and cannot be blank.")
    String name,

    String description,

    @NotNull(message = "Price is mandatory.")
    @DecimalMin(value = "0.01", message = "Price must be positive.")
    BigDecimal price,

    String imgUrl,

    @NotEmpty(message = "Product must belong to at least one category.")
    Set<Long> categoryIds
) {

}
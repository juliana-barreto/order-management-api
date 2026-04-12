package com.juliana_barreto.order_management_api.modules.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
    @NotBlank(message = "Category name is mandatory.")
    @Size(min = 3, max = 80, message = "Name must have between 3 and 80 chars.")
    String name
) {

}

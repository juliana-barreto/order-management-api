package com.juliana_barreto.order_management_api.modules.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordRequest(
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be at least 6 chars.")
    String password
) {

}
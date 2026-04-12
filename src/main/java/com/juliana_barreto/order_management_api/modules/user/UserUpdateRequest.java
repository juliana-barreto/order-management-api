package com.juliana_barreto.order_management_api.modules.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
    @NotBlank(message = "Name is mandatory.")
    String name,

    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Invalid email format.")
    String email,

    @NotBlank(message = "User phone is mandatory.")
    String phone,

    @NotBlank(message = "CPF is mandatory.")
    @Pattern(regexp = "\\d{11}", message = "CPF must contain 11 digits.")
    String cpf
) {

}
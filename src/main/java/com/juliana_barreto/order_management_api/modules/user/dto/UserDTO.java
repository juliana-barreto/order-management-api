package com.juliana_barreto.order_management_api.modules.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.juliana_barreto.order_management_api.modules.user.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO implements Serializable {

  private Long id;

  @NotBlank(message = "Name is mandatory.")
  private String name;

  @NotBlank(message = "Email is mandatory.")
  @Email(message = "Invalid email format.")
  private String email;

  @NotBlank(message = "User phone is mandatory.")
  private String phone;

  @NotBlank(message = "CPF is mandatory.")
  @Pattern(regexp = "\\d{11}", message = "CPF must contain 11 digits.")
  private String cpf;

  @Size(min = 6, message = "Password must be at least 6 chars.")
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;

  public UserDTO(User entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.email = entity.getEmail();
    this.phone = entity.getPhone();
    this.cpf = entity.getCpf();
    // Password is intentionally not mapped from entity to DTO for security reasons
  }
}
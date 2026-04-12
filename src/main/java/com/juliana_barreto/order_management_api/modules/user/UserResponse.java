package com.juliana_barreto.order_management_api.modules.user;

public record UserResponse(
    Long id,

    String name,

    String email,
    String phone,

    String cpf
) {

  public UserResponse(User entity) {
    this(
        entity.getId(),
        entity.getName(),
        entity.getEmail(),
        entity.getPhone(),
        entity.getCpf()
    );
  }
}


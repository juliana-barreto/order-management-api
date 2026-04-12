package com.juliana_barreto.order_management_api.modules.category;

public record CategoryResponse(
    Long id,
    String name
) {

  // Custom constructor mapping Entity to Record
  public CategoryResponse(Category entity) {
    this(
        entity.getId(),
        entity.getName()
    );
  }
}
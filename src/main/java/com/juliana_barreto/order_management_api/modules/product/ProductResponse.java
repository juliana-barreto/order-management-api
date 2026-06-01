package com.juliana_barreto.order_management_api.modules.product;

import com.juliana_barreto.order_management_api.modules.category.Category;
import com.juliana_barreto.order_management_api.modules.category.CategoryResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String imgUrl,
    Set<CategoryResponse> categories
) {

  public ProductResponse(Product entity) {
    this(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getPrice(),
        entity.getImgUrl(),
        entity.getCategories() != null
            ? toCategoriesResponse(entity.getCategories())
            : Collections.emptySet()
    );
  }

  private static Set<CategoryResponse> toCategoriesResponse(Set<Category> entityCategories) {
    Set<CategoryResponse> dtoList = new HashSet<>();
    for (Category category : entityCategories) {
      dtoList.add(new CategoryResponse(category));
    }
    return dtoList;
  }
}
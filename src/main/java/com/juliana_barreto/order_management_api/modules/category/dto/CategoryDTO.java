package com.juliana_barreto.order_management_api.modules.category.dto;

import com.juliana_barreto.order_management_api.modules.category.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryDTO implements Serializable {

  private Long id;

  @NotBlank(message = "Category name is mandatory.")
  @Size(min = 3, max = 80, message = "Name must have between 3 and 80 chars.")
  private String name;

  public CategoryDTO(Category entity) {
    this.id = entity.getId();
    this.name = entity.getName();
  }
}
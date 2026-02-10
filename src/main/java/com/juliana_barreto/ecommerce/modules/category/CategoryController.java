package com.juliana_barreto.ecommerce.modules.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category management")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  @Operation(summary = "List all", description = "Returns the list of all registered categories")
  public ResponseEntity<List<CategoryDTO>> list() {
    return ResponseEntity.ok(categoryService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find by ID", description = "Returns a specific category by its ID")
  public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create category", description = "Creates a new category")
  public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto) {
    CategoryDTO newDto = categoryService.create(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(newDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update category", description = "Updates category status")
  public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
    return ResponseEntity.ok(categoryService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete category",
      description = "Removes a category (only if no products attached)")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
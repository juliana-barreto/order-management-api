package com.juliana_barreto.order_management_api.modules.product.controllers;

import com.juliana_barreto.order_management_api.modules.product.services.ProductService;
import com.juliana_barreto.order_management_api.modules.product.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/products")
@Tag(name = "Products", description = "Product management")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @Operation(summary = "List all", description = "Returns the list of all registered products")
  public ResponseEntity<List<ProductDTO>> list() {
    return ResponseEntity.ok(productService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find by ID", description = "Returns a specific product by its ID")
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create product", description = "Creates a new product with categories")
  public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto) {
    ProductDTO newDto = productService.create(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(newDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update product",
      description = "Updates product details and category associations")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
    return ResponseEntity.ok(productService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete product",
      description = "Removes a product (only if not linked to orders)")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
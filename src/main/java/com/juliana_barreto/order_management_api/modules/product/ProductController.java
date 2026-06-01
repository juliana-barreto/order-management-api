package com.juliana_barreto.order_management_api.modules.product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  @Operation(summary = "List all active", description = "Returns a list of all active products with their categories")
  public ResponseEntity<List<ProductResponse>> listActive() {
    return ResponseEntity.ok(productService.findAllActive());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find by ID", description = "Returns a specific product by its ID")
  public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create product", description = "Creates a new product with categories")
  public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
    ProductResponse response = productService.create(request);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update product",
      description = "Updates product details and category associations")
  public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(productService.update(id, request));
  }

  @PatchMapping("/{id}/deactivate")
  @Operation(summary = "Deactivate product", description = "Soft deletes a product")
  public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    productService.deactivate(id);
    return ResponseEntity.noContent().build();
  }
}
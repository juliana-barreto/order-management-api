package com.juliana_barreto.ecommerce.modules.product;

import com.juliana_barreto.ecommerce.modules.category.Category;
import com.juliana_barreto.ecommerce.modules.category.CategoryRepository;
import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public List<Product> findAll() {
    return productRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Product findById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
  }

  @Transactional
  public Product create(Product product) {

    // Basic validation
    if (product.getName() == null || product.getName().isBlank()) {
      throw new IllegalArgumentException("Product name is mandatory.");
    }
    if (product.getPrice() == null) {
      throw new IllegalArgumentException("Product price is mandatory.");
    }

    // Handle Category Association
    if (product.getCategories() != null && !product.getCategories().isEmpty()) {
      Set<Category> categoriesRequest = new HashSet<>(product.getCategories());
      product.getCategories().clear(); // Prevent saving transient/detached objects
      assignCategoriesToProduct(product, categoriesRequest);
    }
    return productRepository.save(product);
  }

  @Transactional
  public Product update(Long id, Product updatedProduct) {
    Product existingProduct = findById(id);

    // Update simple fields
    if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank()) {
      existingProduct.setName(updatedProduct.getName());
    }
    if (updatedProduct.getDescription() != null && !updatedProduct.getDescription().isBlank()) {
      existingProduct.setDescription(updatedProduct.getDescription());
    }
    if (updatedProduct.getPrice() != null) {
      existingProduct.setPrice(updatedProduct.getPrice());
    }
    if (updatedProduct.getImgUrl() != null && !updatedProduct.getImgUrl().isBlank()) {
      existingProduct.setImgUrl(updatedProduct.getImgUrl());
    }

    // Update Category Association
    if (updatedProduct.getCategories() != null) {
      existingProduct.getCategories().clear(); // Clear existing relationships
      assignCategoriesToProduct(existingProduct, updatedProduct.getCategories());
    }

    return productRepository.save(existingProduct);
  }

  @Transactional
  public void delete(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ResourceNotFoundException("Product not found for deletion.");
    }
    try {
      productRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException(
          "Integrity violation: Unable to delete product because it is part of existing orders.");
    }
  }

  // Helper method to fetch Category entities from the DB and associate them with the Product
  private void assignCategoriesToProduct(Product product, Set<Category> categories) {
    for (Category catStub : categories) {
      Category category = categoryRepository.findById(catStub.getId())
          .orElseThrow(() -> new ResourceNotFoundException(
              "Category not found with ID: " + catStub.getId()));
      product.getCategories().add(category);
    }
  }
}
package com.juliana_barreto.order_management_api.modules.product;

import com.juliana_barreto.order_management_api.modules.category.Category;
import com.juliana_barreto.order_management_api.modules.category.CategoryRepository;
import com.juliana_barreto.order_management_api.modules.category.CategoryResponse;
import com.juliana_barreto.order_management_api.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<ProductResponse> findAllActive() {
    // Fetch only active products using custom repository method
    List<Product> entities = productRepository.findAllActive();
    List<ProductResponse> responses = new ArrayList<>();

    for (Product entity : entities) {
      responses.add(new ProductResponse(entity));
    }
    return responses;
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> findActiveByCategory(Long categoryId) {
    // Fetch products filtered by category
    List<Product> entities = productRepository.findActiveProductsByCategory(categoryId);
    List<ProductResponse> responses = new ArrayList<>();

    for (Product entity : entities) {
      responses.add(new ProductResponse(entity));
    }
    return responses;
  }

  @Transactional(readOnly = true)
  public ProductResponse findById(Long id) {
    Product entity = productRepository.findByIdWithCategories(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    return new ProductResponse(entity);
  }

  @Transactional
  public ProductResponse create(ProductRequest request) {

    Product entity = new Product();
    entity.setName(request.name());
    entity.setDescription(request.description());
    entity.setImgUrl(request.imgUrl());
    entity.updatePrice(request.price());

    handleCategoryAssociation(request.categoryIds(), entity);

    entity = productRepository.save(entity);
    return mapToResponse(entity);
  }

  @Transactional
  public ProductResponse update(Long id, ProductRequest request) {
    Product entity = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

    entity.setName(request.name());
    entity.setDescription(request.description());
    entity.setImgUrl(request.imgUrl());
    entity.updatePrice(request.price());

    handleCategoryAssociation(request.categoryIds(), entity);

    entity = productRepository.save(entity);
    return mapToResponse(entity);
  }

  // Soft delete approach replacing the hard delete
  @Transactional
  public void deactivate(Long id) {
    Product entity = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found for deactivation."));

    // The entity manages its own state transition
    entity.deactivate();
    productRepository.save(entity);
  }

  private void handleCategoryAssociation(Set<Long> categoryIds, Product entity) {
    entity.getCategories().clear();
    if (categoryIds != null && !categoryIds.isEmpty()) {
      for (Long catId : categoryIds) {
        Category category = categoryRepository.findById(catId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Category not found with ID: " + catId));
        entity.getCategories().add(category);
      }
    }
  }

  private ProductResponse mapToResponse(Product entity) {
    Set<CategoryResponse> categoryResponses = new HashSet<>();

    if (entity.getCategories() != null) {
      for (Category category : entity.getCategories()) {
        categoryResponses.add(new CategoryResponse(category));
      }
    }

    return new ProductResponse(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getPrice(),
        entity.getImgUrl(),
        categoryResponses
    );
  }
}
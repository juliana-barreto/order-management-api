package com.juliana_barreto.ecommerce.modules.product;

import com.juliana_barreto.ecommerce.modules.category.Category;
import com.juliana_barreto.ecommerce.modules.category.CategoryDTO;
import com.juliana_barreto.ecommerce.modules.category.CategoryRepository;
import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
  public List<ProductDTO> findAll() {
    List<Product> entities = productRepository.findAll();
    List<ProductDTO> dtos = new ArrayList<>();
    for (Product entity : entities) {
      dtos.add(new ProductDTO(entity));
    }
    return dtos;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Product entity = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    return new ProductDTO(entity);
  }

  @Transactional
  public ProductDTO create(ProductDTO dto) {
    // Basic validation
    if (dto.getName() == null || dto.getName().isBlank()) {
      throw new IllegalArgumentException("Product name is mandatory.");
    }
    if (dto.getPrice() == null) {
      throw new IllegalArgumentException("Product price is mandatory.");
    }

    Product entity = new Product();
    copyDtoToEntity(dto, entity);
    entity = productRepository.save(entity);
    return new ProductDTO(entity);
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO dto) {
    Product entity = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

    // Update simple fields
    if (dto.getName() != null && !dto.getName().isBlank()) {
      entity.setName(dto.getName());
    }
    if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
      entity.setDescription(dto.getDescription());
    }
    if (dto.getPrice() != null) {
      entity.setPrice(dto.getPrice());
    }
    if (dto.getImgUrl() != null && !dto.getImgUrl().isBlank()) {
      entity.setImgUrl(dto.getImgUrl());
    }

    // Update Category Association
    if (dto.getCategories() != null) {
      entity.getCategories().clear();
      for (CategoryDTO catDto : dto.getCategories()) {
        Category category = categoryRepository.findById(catDto.getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Category not found with ID: " + catDto.getId()));
        entity.getCategories().add(category);
      }
    }

    entity = productRepository.save(entity);
    return new ProductDTO(entity);
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

  // Helper method to copy DTO fields to entity
  private void copyDtoToEntity(ProductDTO dto, Product entity) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrice(dto.getPrice());
    entity.setImgUrl(dto.getImgUrl());

    // Handle Category Association
    if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
      entity.getCategories().clear();
      for (CategoryDTO catDto : dto.getCategories()) {
        Category category = categoryRepository.findById(catDto.getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Category not found with ID: " + catDto.getId()));
        entity.getCategories().add(category);
      }
    }
  }
}
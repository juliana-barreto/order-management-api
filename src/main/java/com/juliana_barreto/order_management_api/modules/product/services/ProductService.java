package com.juliana_barreto.order_management_api.modules.product.services;

import com.juliana_barreto.order_management_api.modules.category.entities.Category;
import com.juliana_barreto.order_management_api.modules.category.dto.CategoryDTO;
import com.juliana_barreto.order_management_api.modules.category.repositories.CategoryRepository;
import com.juliana_barreto.order_management_api.modules.order.entities.Order;
import com.juliana_barreto.order_management_api.modules.order.repositories.OrderRepository;
import com.juliana_barreto.order_management_api.modules.product.dto.ProductDTO;
import com.juliana_barreto.order_management_api.modules.product.entities.Product;
import com.juliana_barreto.order_management_api.modules.product.repositories.ProductRepository;
import com.juliana_barreto.order_management_api.shared.exceptions.DatabaseException;
import com.juliana_barreto.order_management_api.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final OrderRepository orderRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
      OrderRepository orderRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.orderRepository = orderRepository;
  }

  @Transactional(readOnly = true)
  public List<ProductDTO> findAll() {
    List<Product> entities = productRepository.findAllWithCategories();
    List<ProductDTO> dtos = new ArrayList<>();
    for (Product entity : entities) {
      dtos.add(new ProductDTO(entity));
    }
    return dtos;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id) {
    Product entity = productRepository.findByIdWithCategories(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    return new ProductDTO(entity);
  }

  @Transactional
  public ProductDTO create(ProductDTO dto) {

    Product entity = new Product();
    copyDtoToEntity(dto, entity);
    entity = productRepository.save(entity);
    return new ProductDTO(entity);
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO dto) {
    Product entity = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrice(dto.getPrice());
    entity.setImgUrl(dto.getImgUrl());

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

    List<Order> orders = orderRepository.findOrdersByProductId(id);
    if (!orders.isEmpty()) {
      throw new DatabaseException(
          "Integrity violation: Unable to delete product because it is part of existing orders.");
    }

    try {
      productRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException(
          "Integrity violation: Something went wrong during deletion.");
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
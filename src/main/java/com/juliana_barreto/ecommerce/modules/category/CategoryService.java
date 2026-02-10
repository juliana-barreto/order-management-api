package com.juliana_barreto.ecommerce.modules.category;

import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public List<CategoryDTO> findAll() {
    List<Category> entities = categoryRepository.findAll();
    List<CategoryDTO> dtos = new ArrayList<>();
    for (Category entity : entities) {
      dtos.add(new CategoryDTO(entity));
    }
    return dtos;
  }

  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    Category entity = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO create(CategoryDTO dto) {
    if (dto.getName() == null || dto.getName().isBlank()) {
      throw new IllegalArgumentException("Category name is mandatory.");
    }
    Category entity = new Category();
    entity.setName(dto.getName());
    entity = categoryRepository.save(entity);
    return new CategoryDTO(entity);
  }

  @Transactional
  public CategoryDTO update(Long id, CategoryDTO dto) {
    Category entity = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

    if (dto.getName() != null && !dto.getName().isBlank()) {
      entity.setName(dto.getName());
    }
    entity = categoryRepository.save(entity);
    return new CategoryDTO(entity);
  }

  @Transactional
  public void delete(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Category not found for deletion.");
    }
    try {
      categoryRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException(
          "Integrity violation: Unable to delete category because it has associated products.");
    }
  }
}

package com.juliana_barreto.ecommerce.modules.category;

import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
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
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Category findById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
  }

  @Transactional
  public Category create(Category category) {
    if (category.getName() == null || category.getName().isBlank()) {
      throw new IllegalArgumentException("Category name is mandatory.");
    }
    return categoryRepository.save(category);
  }

  @Transactional
  public Category update(Long id, Category updatedCategory) {
    Category existingCategory = findById(id);

    if (updatedCategory.getName() != null && !updatedCategory.getName().isBlank()) {
      existingCategory.setName(updatedCategory.getName());
    }
    return categoryRepository.save(existingCategory);
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

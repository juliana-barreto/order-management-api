package com.juliana_barreto.order_management_api.modules.category;
import com.juliana_barreto.order_management_api.shared.exceptions.DatabaseException;
import com.juliana_barreto.order_management_api.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<CategoryResponse> findAll() {
    List<Category> entities = categoryRepository.findAll();
    List<CategoryResponse> responses = new ArrayList<>();
    for (Category entity : entities) {
      responses.add(new CategoryResponse(entity));
    }
    return responses;
  }

  @Transactional(readOnly = true)
  public CategoryResponse findById(Long id) {
    Category entity = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    return new CategoryResponse(entity);
  }

  @Transactional
  public CategoryResponse create(CategoryRequest request) {
    Category entity = new Category();
    entity.setName(request.name());
    entity = categoryRepository.save(entity);
    return new CategoryResponse(entity);
  }

  @Transactional
  public CategoryResponse update(Long id, CategoryRequest request) {
    Category entity = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    entity.setName(request.name());
    entity = categoryRepository.save(entity);
    return new CategoryResponse(entity);
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

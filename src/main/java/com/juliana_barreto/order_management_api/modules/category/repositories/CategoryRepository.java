package com.juliana_barreto.order_management_api.modules.category.repositories;

import com.juliana_barreto.order_management_api.modules.category.entities.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByName(String name);
}

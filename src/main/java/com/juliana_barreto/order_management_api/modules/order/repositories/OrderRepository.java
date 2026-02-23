package com.juliana_barreto.order_management_api.modules.order.repositories;

import com.juliana_barreto.order_management_api.modules.order.entities.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN o.items i
        WHERE i.id.product.id = :productId
      """)
  List<Order> findOrdersByProductId(@Param("productId") Long productId);

  @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN FETCH o.client
        JOIN FETCH o.items item
        JOIN FETCH item.id.product
      """)
  List<Order> findAllWithRelations();

  @Query("""
          SELECT o
          FROM Order o
          JOIN FETCH o.client
          JOIN FETCH o.items item
          JOIN FETCH item.id.product
          WHERE o.id = :id
      """)
  Optional<Order> findByIdWithRelations(@Param("id") Long id);
}


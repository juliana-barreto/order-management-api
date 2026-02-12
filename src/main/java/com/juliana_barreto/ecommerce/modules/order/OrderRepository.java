package com.juliana_barreto.ecommerce.modules.order;

import java.util.List;
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
}


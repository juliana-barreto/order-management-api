package com.juliana_barreto.order_management_api.modules.order.repositories;

import com.juliana_barreto.order_management_api.modules.order.entities.OrderItem;
import com.juliana_barreto.order_management_api.modules.order.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}


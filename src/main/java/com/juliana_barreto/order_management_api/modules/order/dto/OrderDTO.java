package com.juliana_barreto.order_management_api.modules.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juliana_barreto.order_management_api.modules.order.entities.OrderItem;
import com.juliana_barreto.order_management_api.modules.order.OrderStatus;
import com.juliana_barreto.order_management_api.modules.order.entities.Order;
import com.juliana_barreto.order_management_api.modules.user.dto.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDTO implements Serializable {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
  private Instant moment;

  private OrderStatus status;

  @NotNull(message = "Client is mandatory.")
  private UserDTO client;

  @Valid
  @NotEmpty(message = "Order must have at least one item.")
  private List<OrderItemDTO> items = new ArrayList<>();

  private BigDecimal total;

  public OrderDTO(Order entity) {
    this.id = entity.getId();
    this.moment = entity.getMoment();
    this.status = entity.getStatus();
    this.client = new UserDTO(entity.getClient());
    this.total = entity.getTotal();

    // Convert order items from entity to DTO
    if (entity.getItems() != null) {
      this.items = new ArrayList<>();

      for (OrderItem itemEntity : entity.getItems()) {
        this.items.add(new OrderItemDTO(itemEntity));
      }
    }
  }
}
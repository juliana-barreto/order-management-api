package com.juliana_barreto.order_management_api.modules.order.controllers;

import com.juliana_barreto.order_management_api.modules.order.dto.OrderDTO;
import com.juliana_barreto.order_management_api.modules.order.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Sales order management")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  @Operation(summary = "List all", description = "Returns the list of all registered orders")
  public ResponseEntity<List<OrderDTO>> list() {
    return ResponseEntity.ok(orderService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find by ID", description = "Returns a specific order by its ID")
  public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create order", description = "Creates a new order for an existing user")
  public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO dto) {
    OrderDTO newDto = orderService.create(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(newDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newDto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update order", description = "Updates general order status")
  public ResponseEntity<OrderDTO> update(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
    return ResponseEntity.ok(orderService.update(id, dto));
  }

  @PatchMapping("/{id}/cancel")
  @Operation(summary = "Cancel order",
      description = "Cancels an order if it hasn't been shipped yet")
  public ResponseEntity<OrderDTO> cancel(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.cancel(id));
  }
}

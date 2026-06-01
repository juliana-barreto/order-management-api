package com.juliana_barreto.order_management_api.modules.order;

import com.juliana_barreto.order_management_api.modules.order.dto.OrderRequest;
import com.juliana_barreto.order_management_api.modules.order.dto.OrderResponse;
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
  public ResponseEntity<List<OrderResponse>> list() {
    return ResponseEntity.ok(orderService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find by ID", description = "Returns a specific order by its ID")
  public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create order", description = "Creates a new order for an existing user")
  public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
    OrderResponse response = orderService.create(request);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{id}/pay")
  @Operation(summary = "Mark order as paid", description = "Marks an order as paid if it hasn't been canceled or shipped yet")
  public ResponseEntity<OrderResponse> pay(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.markAsPaid(id));
  }

  @PatchMapping("/{id}/ship")
  @Operation(summary = "Mark order as shipped", description = "Marks an order as shipped if it has been paid and hasn't been canceled yet")
  public ResponseEntity<OrderResponse> ship(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.shipOrder(id));
  }

  @PatchMapping("/{id}/cancel")
  @Operation(summary = "Cancel order",
      description = "Cancels an order if it hasn't been shipped yet")
  public ResponseEntity<OrderResponse> cancel(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.cancel(id));
  }
}

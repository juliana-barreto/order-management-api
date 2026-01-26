package com.senai.ecommerce.modules.pedido;

import java.net.URI;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gestão de pedidos de venda")
public class PedidoController {

  private final PedidoService pedidoService;

  public PedidoController(PedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @GetMapping
  @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos")
  public ResponseEntity<List<Pedido>> listar() {
    return ResponseEntity.ok(pedidoService.buscarTodos());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar pedido", description = "Busca pedido por ID")
  public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
    return ResponseEntity.ok(pedidoService.buscarPorId(id));
  }

  @PostMapping
  @Operation(summary = "Criar pedido", description = "Cria um novo pedido para um cliente existente")
  public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
    Pedido novoPedido = pedidoService.criar(pedido);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(novoPedido.getId()).toUri();
    return ResponseEntity.created(uri).body(novoPedido);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar pedido", description = "Atualiza status do pedido")
  public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
    return ResponseEntity.ok(pedidoService.atualizar(id, pedido));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Cancelar/Deletar pedido", description = "Remove o pedido do sistema")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    pedidoService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}


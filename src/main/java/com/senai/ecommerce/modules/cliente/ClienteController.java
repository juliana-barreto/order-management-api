package com.senai.ecommerce.modules.cliente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
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
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public class ClienteController {

  private final ClienteService clienteService;

  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  @GetMapping
  @Operation(summary = "Listar todos", description = "Retorna a lista de todos os clientes cadastrados")
  public ResponseEntity<List<Cliente>> listar() {
    return ResponseEntity.ok(clienteService.buscarTodos());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar por ID", description = "Retorna um cliente específico pelo seu ID")
  public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
    return ResponseEntity.ok(clienteService.buscarPorId(id));
  }

  @PostMapping
  @Operation(summary = "Criar cliente", description = "Cria um novo cliente com validação de dados")
  public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
    Cliente novoCliente = clienteService.criar(cliente);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(novoCliente.getId()).toUri();
    return ResponseEntity.created(uri).body(novoCliente);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar cliente", description = "Atualiza apenas os dados fornecidos, mantendo os demais")
  public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
    return ResponseEntity.ok(clienteService.atualizar(id, cliente));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deletar cliente", description = "Remove um cliente da base de dados")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    clienteService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}

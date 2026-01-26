package com.senai.ecommerce.modules.pedido;

import org.springframework.stereotype.Service;
import com.senai.ecommerce.modules.cliente.Cliente;
import com.senai.ecommerce.modules.cliente.ClienteRepository;
import com.senai.ecommerce.shared.exceptions.RecursoNaoEncontradoException;
import java.util.List;

@Service
public class PedidoService {

  private final PedidoRepository pedidoRepository;
  private final ClienteRepository clienteRepository;

  public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
    this.pedidoRepository = pedidoRepository;
    this.clienteRepository = clienteRepository;
  }

  public List<Pedido> buscarTodos() {
    return pedidoRepository.findAll();
  }

  public Pedido buscarPorId(Long id) {
    return pedidoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + id));
  }

  public Pedido criar(Pedido pedido) {
    // Validação básica
    if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
      throw new IllegalArgumentException("O pedido deve estar associado a um cliente existente.");
    }

    // Busca o cliente real no banco para garantir consistência
    Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente do pedido não encontrado."));

    pedido.setCliente(cliente);

    // Associa os itens ao pedido e calcula total
    if (pedido.getItens() != null) {
      for (ItemPedido item : pedido.getItens()) {
        item.setPedido(pedido);
      }
    }

    pedido.calcularValorTotal(); // Seu método da entidade
    return pedidoRepository.save(pedido);
  }

  public Pedido atualizar(Long id, Pedido dadosAtualizados) {
    Pedido pedidoExistente = buscarPorId(id);

    if (dadosAtualizados.getStatus() != null) {
      pedidoExistente.setStatus(dadosAtualizados.getStatus());
    }

    return pedidoRepository.save(pedidoExistente);
  }

  public void deletar(Long id) {
    if (!pedidoRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Pedido não encontrado.");
    }
    pedidoRepository.deleteById(id);
  }
}

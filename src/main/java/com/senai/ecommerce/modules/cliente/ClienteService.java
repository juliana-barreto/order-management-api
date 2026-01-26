package com.senai.ecommerce.modules.cliente;

import com.senai.ecommerce.shared.exceptions.RecursoNaoEncontradoException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

  private final ClienteRepository clienteRepository;

  public ClienteService(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  public List<Cliente> buscarTodos() {
    return clienteRepository.findAll();
  }

  public Cliente buscarPorId(Long id) {
    return clienteRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));
  }

  public Cliente criar(Cliente cliente) {
    if (cliente.getNome() == null || cliente.getNome().isBlank()) {
      throw new IllegalArgumentException("O nome do cliente é obrigatório.");
    }
    if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
      throw new IllegalArgumentException("O CPF do cliente é obrigatório.");
    }
    return clienteRepository.save(cliente);
  }

  public Cliente atualizar(Long id, Cliente clienteAtualizado) {
    Cliente clienteExistente = buscarPorId(id);

    // Atualiza apenas se o campo não for nulo
    if (clienteAtualizado.getNome() != null && !clienteAtualizado.getNome().isBlank()) {
      clienteExistente.setNome(clienteAtualizado.getNome());
    }
    if (clienteAtualizado.getEmail() != null && !clienteAtualizado.getEmail().isBlank()) {
      clienteExistente.setEmail(clienteAtualizado.getEmail());
    }

    return clienteRepository.save(clienteExistente);
  }

  public void deletar(Long id) {
    if (!clienteRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Cliente não encontrado para deleção.");
    }
    clienteRepository.deleteById(id);
  }
}

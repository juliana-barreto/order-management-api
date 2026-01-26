package com.senai.ecommerce.modules.pedido;

import com.senai.ecommerce.modules.cliente.Cliente;
import com.senai.ecommerce.modules.cliente.StatusPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valorTotal;

  @Column(nullable = false)
  private LocalDateTime dataPedido = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemPedido> itens = new ArrayList<>();

  public void addItem(ItemPedido item) {
    this.itens.add(item);
    item.setPedido(this);
    calcularValorTotal();
  }

  public void removeItem(ItemPedido item) {
    this.itens.remove(item);
    item.setPedido(null);
    calcularValorTotal();
  }

  public void calcularValorTotal() {
    var total = BigDecimal.ZERO;
    for (ItemPedido item : this.itens) {
      var quantity = BigDecimal.valueOf(item.getQuantidade());
      BigDecimal itemTotal = item.getPrecoUnitario().multiply(quantity);
      total = total.add(itemTotal);
    }
    this.valorTotal = total;
  }

}

package com.senai.ecommerce.modules.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemPedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nomeProduto;

  @Column(nullable = false)
  private Integer quantidade;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal precoUnitario;

  @ManyToOne
  @JoinColumn(name = "pedido_id", nullable = false)
  @JsonIgnore
  private Pedido pedido;
}

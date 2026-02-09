package com.juliana_barreto.ecommerce.modules.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juliana_barreto.ecommerce.modules.user.User;
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
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal orderTotal;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
  @Column(nullable = false)
  private Instant moment = Instant.now();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status = OrderStatus.AWAITING_PAYMENT;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private User client;

  @Setter(AccessLevel.NONE)
  @Builder.Default
  @OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrderItem> items = new HashSet<>();
}

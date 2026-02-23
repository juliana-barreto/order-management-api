package com.juliana_barreto.order_management_api.modules.order.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include
  @Id
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false)
  private Instant moment = Instant.now();

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Order order;
}

package com.juliana_barreto.order_management_api.modules.product.entities;

import com.juliana_barreto.order_management_api.modules.category.entities.Category;
import com.juliana_barreto.order_management_api.modules.order.entities.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long Id;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private String imgUrl;

  @Setter(AccessLevel.NONE)
  @Builder.Default
  @ManyToMany
  @JoinTable(name = "tb_product_category",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  @Setter(AccessLevel.NONE)
  @Builder.Default
  @OneToMany(mappedBy = "id.product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrderItem> items = new HashSet<>();

}

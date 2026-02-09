package com.juliana_barreto.ecommerce;

import com.juliana_barreto.ecommerce.modules.category.Category;
import com.juliana_barreto.ecommerce.modules.category.CategoryRepository;
import com.juliana_barreto.ecommerce.modules.order.Order;
import com.juliana_barreto.ecommerce.modules.order.OrderRepository;
import com.juliana_barreto.ecommerce.modules.order.OrderStatus;
import com.juliana_barreto.ecommerce.modules.order_item.OrderItem;
import com.juliana_barreto.ecommerce.modules.order_item.OrderItemRepository;
import com.juliana_barreto.ecommerce.modules.order.Payment;
import com.juliana_barreto.ecommerce.modules.product.Product;
import com.juliana_barreto.ecommerce.modules.product.ProductRepository;
import com.juliana_barreto.ecommerce.modules.user.User;
import com.juliana_barreto.ecommerce.modules.user.UserRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Override
  public void run(String... args) throws Exception {
    User u1 = User.builder()
        .name("Maria Brown")
        .email("maria@gmail.com")
        .phone("988888888")
        .cpf("19999999991")
        .password("123456")
        .build();

    User u2 = User.builder()
        .name("Alex Green")
        .email("alex@gmail.com")
        .phone("977777777")
        .cpf("12345678902")
        .password("123456")
        .build();

    userRepository.saveAll(List.of(u1, u2));

    Order o1 = Order.builder()
        .moment(Instant.parse("2019-06-20T19:53:07Z"))
        .status(OrderStatus.PAID)
        .client(u1)
        .build();

    Order o2 = Order.builder()
        .moment(Instant.parse("2019-07-21T03:42:10Z"))
        .status(OrderStatus.AWAITING_PAYMENT)
        .client(u2)
        .build();

    Order o3 = Order.builder()
        .moment(Instant.parse("2019-07-22T15:21:22Z"))
        .status(OrderStatus.AWAITING_PAYMENT)
        .client(u1)
        .build();

    orderRepository.saveAll(List.of(o1, o2, o3));

    Category cat1 = Category.builder()
        .name("Electronics")
        .build();

    Category cat2 = Category.builder()
        .name("Books")
        .build();

    Category cat3 = Category.builder()
        .name("Computers")
        .build();

    categoryRepository.saveAll(List.of(cat1, cat2, cat3));

    Product p1 = Product.builder()
        .name("The Lord of the Rings")
        .description("Lorem ipsum dolor sit amet, consectetur.")
        .price(90.5)
        .build();

    Product p2 = Product.builder()
        .name("Smart TV")
        .description("Nulla eu imperdiet purus. Maecenas ante.")
        .price(2190.0)
        .build();

    Product p3 = Product.builder()
        .name("Macbook Pro")
        .description("Nam eleifend maximus tortor, at mollis.")
        .price(1250.0)
        .build();

    Product p4 = Product.builder()
        .name("PC Gamer")
        .description("Donec aliquet odio ac rhoncus cursus.")
        .price(1200.0)
        .build();

    Product p5 = Product.builder()
        .name("Rails for Dummies")
        .description("Cras fringilla convallis sem vel faucibus.")
        .price(100.99)
        .build();

    productRepository.saveAll(List.of(p1, p2, p3, p4, p5));

    p1.getCategories().add(cat2);
    p2.getCategories().add(cat1);
    p2.getCategories().add(cat3);
    p3.getCategories().add(cat3);
    p4.getCategories().add(cat3);
    p5.getCategories().add(cat2);

    productRepository.saveAll(List.of(p1, p2, p3, p4, p5));

    OrderItem oi1 = OrderItem.builder()
        .order(o1)
        .product(p1)
        .quantity(2)
        .unitPrice(p1.getPrice())
        .build();

    OrderItem oi2 = OrderItem.builder()
        .order(o1)
        .product(p3)
        .quantity(1)
        .unitPrice(p3.getPrice())
        .build();

    OrderItem oi3 = OrderItem.builder()
        .order(o2)
        .product(p3)
        .quantity(2)
        .unitPrice(p3.getPrice())
        .build();

    OrderItem oi4 = OrderItem.builder()
        .order(o3)
        .product(p5)
        .quantity(2)
        .unitPrice(p5.getPrice())
        .build();

    orderItemRepository.saveAll(List.of(oi1, oi2, oi3, oi4));

    Payment pay1 = Payment.builder()
        .moment(Instant.parse("2019-06-10T21:53:07Z"))
        .build();

    o1.setPayment(pay1);
    orderRepository.save(o1);
  }
}

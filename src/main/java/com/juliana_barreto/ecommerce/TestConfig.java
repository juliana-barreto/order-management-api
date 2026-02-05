package com.juliana_barreto.ecommerce;

import com.juliana_barreto.ecommerce.modules.user.User;
import com.juliana_barreto.ecommerce.modules.user.UserRepository;
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
  }
}

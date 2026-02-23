package com.juliana_barreto.order_management_api.modules.user.repositories;

import com.juliana_barreto.order_management_api.modules.user.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByCpf(String cpf);

  Optional<User> findByPhone(String phone);
}

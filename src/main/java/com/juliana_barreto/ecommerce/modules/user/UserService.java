package com.juliana_barreto.ecommerce.modules.user;

import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
  }

  @Transactional
  public User create(User user) {
    if (user.getName() == null || user.getName().isBlank()) {
      throw new IllegalArgumentException("User name is mandatory.");
    }
    return userRepository.save(user);
  }

  @Transactional
  public User update(Long id, User updatedUser) {
    User existingUser = findById(id);
    
    if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
      existingUser.setName(updatedUser.getName());
    }
    if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
      existingUser.setEmail(updatedUser.getEmail());
    }

    return userRepository.save(existingUser);
  }

  @Transactional
  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found for deletion.");
    }
    try {
      userRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Integrity violation: You cannot delete a user that has orders.");
    }
  }
}

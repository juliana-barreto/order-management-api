package com.juliana_barreto.order_management_api.modules.user;

import com.juliana_barreto.order_management_api.shared.exceptions.BusinessException;
import com.juliana_barreto.order_management_api.shared.exceptions.DatabaseException;
import com.juliana_barreto.order_management_api.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  @Transactional(readOnly = true)
  public List<UserResponse> findAll() {
    List<User> entities = userRepository.findAll();
    List<UserResponse> responses = new ArrayList<>();
    for (User entity : entities) {
      responses.add(new UserResponse(entity));
    }
    return responses;
  }

  @Transactional(readOnly = true)
  public UserResponse findById(Long id) {
    User entity = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    return new UserResponse(entity);
  }

  @Transactional
  public UserResponse create(UserRequest request) {
    validateUniqueFields(request.email(), request.cpf(), request.phone(), null);

    User entity = new User();
    entity.setName(request.name());
    entity.setCpf(request.cpf());
    entity.updateContactInfo(request.email(), request.phone());
    entity.changePassword(passwordEncoder.encode(request.password()));

    entity = userRepository.save(entity);
    return new UserResponse(entity);
  }

  // Specific Use Case: Update only contact and basic info
  @Transactional
  public UserResponse updateBasicInfo(Long id, UserUpdateRequest request) {
    User entity = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    validateUniqueFields(request.email(), request.cpf(), request.phone(), id);

    entity.setName(request.name());
    entity.updateContactInfo(request.email(), request.phone());
    entity = userRepository.save(entity);

    return new UserResponse(entity);
  }

  // Specific Use Case: Change password securely
  @Transactional
  public void updatePassword(Long id, UserPasswordRequest request) {
    User entity = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    entity.changePassword(passwordEncoder.encode(request.password()));
    userRepository.save(entity);
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

  // Helper method to validate unique fields
  private void validateUniqueFields(String email, String cpf, String phone, Long currentId) {
    userRepository.findByEmail(email).ifPresent(user -> {
      if (!user.getId().equals(currentId)) {
        throw new BusinessException("Email already registered.");
      }
    });

    userRepository.findByCpf(cpf).ifPresent(user -> {
      if (!user.getId().equals(currentId)) {
        throw new BusinessException("CPF already registered.");
      }
    });

    userRepository.findByPhone(phone).ifPresent(user -> {
      if (!user.getId().equals(currentId)) {
        throw new BusinessException("Phone already registered.");
      }
    });
  }
}

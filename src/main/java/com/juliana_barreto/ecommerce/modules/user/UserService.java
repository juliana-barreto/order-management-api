package com.juliana_barreto.ecommerce.modules.user;

import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
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
  public List<UserDTO> findAll() {
    List<User> entities = userRepository.findAll();
    List<UserDTO> dtos = new ArrayList<>();
    for (User entity : entities) {
      dtos.add(new UserDTO(entity));
    }
    return dtos;
  }

  @Transactional(readOnly = true)
  public UserDTO findById(Long id) {
    User entity = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO create(UserDTO dto) {
    if (dto.getName() == null || dto.getName().isBlank()) {
      throw new IllegalArgumentException("User name is mandatory.");
    }
    User entity = new User();
    copyDtoToEntity(dto, entity);
    entity = userRepository.save(entity);
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO update(Long id, UserDTO dto) {
    User entity = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    if (dto.getName() != null && !dto.getName().isBlank()) {
      entity.setName(dto.getName());
    }
    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      entity.setEmail(dto.getEmail());
    }
    if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
      entity.setPhone(dto.getPhone());
    }
    if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
      entity.setCpf(dto.getCpf());
    }
    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
      entity.setPassword(dto.getPassword());
    }
    entity = userRepository.save(entity);
    return new UserDTO(entity);
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

  // Helper method to copy DTO fields to entity
  private void copyDtoToEntity(UserDTO dto, User entity) {
    entity.setName(dto.getName());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone());
    entity.setCpf(dto.getCpf());
    entity.setPassword(dto.getPassword());
  }
}

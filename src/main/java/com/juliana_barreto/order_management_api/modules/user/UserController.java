package com.juliana_barreto.order_management_api.modules.user;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "Users", description = "User management")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "List all", description = "Returns the list of all registered users")
  public ResponseEntity<List<UserResponse>> list() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find by ID", description = "Returns a specific user by their ID")
  public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Create user", description = "Creates a new user")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
    UserResponse response = userService.create(request);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update basic info", description = "Updates name, email, phone and cpf")
  public ResponseEntity<UserResponse> updateBasicInfo(
      @PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(userService.updateBasicInfo(id, request));
  }

  @PatchMapping("/{id}/password")
  @Operation(summary = "Change password")
  public ResponseEntity<Void> updatePassword(
      @PathVariable Long id, @Valid @RequestBody UserPasswordRequest request) {
    userService.updatePassword(id, request);
    return ResponseEntity.noContent().build();
  }
  
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user", description = "Removes a user (only if they have no orders)")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

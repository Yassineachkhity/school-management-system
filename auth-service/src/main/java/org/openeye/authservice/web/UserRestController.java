package org.openeye.authservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.authservice.dtos.UserCreateRequest;
import org.openeye.authservice.dtos.UserDTO;
import org.openeye.authservice.dtos.UserLastLoginUpdateRequest;
import org.openeye.authservice.dtos.UserPasswordUpdateRequest;
import org.openeye.authservice.dtos.UserUpdateRequest;
import org.openeye.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/by-email")
    public UserDTO getUserByEmail(@RequestParam @NotBlank @Email String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/search")
    public List<UserDTO> searchUsers(@RequestParam @NotBlank String keyword) {
        return userService.searchUsers(keyword);
    }

    @PostMapping("/sync")
    public List<UserDTO> syncUsersFromKeycloak() {
        return userService.syncUsersFromKeycloak();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO created = userService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(created.getUserId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{userId}")
    public UserDTO updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable String userId,
            @Valid @RequestBody UserPasswordUpdateRequest request
    ) {
        userService.updatePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/last-login")
    public UserDTO updateLastLogin(
            @PathVariable String userId,
            @Valid @RequestBody UserLastLoginUpdateRequest request
    ) {
        return userService.updateLastLogin(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

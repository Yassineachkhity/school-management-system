package org.openeye.authservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.authservice.dtos.UserRoleDTO;
import org.openeye.authservice.dtos.UserRoleRequest;
import org.openeye.authservice.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleRestController {

    private final UserRoleService userRoleService;

    @GetMapping
    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleService.getAllUserRoles();
    }

    @GetMapping("/by-user")
    public List<UserRoleDTO> getRolesByUser(@RequestParam @NotBlank String userId) {
        return userRoleService.getRolesByUserId(userId);
    }

    @GetMapping("/by-role")
    public List<UserRoleDTO> getUsersByRole(@RequestParam @NotBlank String roleId) {
        return userRoleService.getUsersByRoleId(roleId);
    }

    @PostMapping
    public UserRoleDTO assignRole(@Valid @RequestBody UserRoleRequest request) {
        return userRoleService.assignRole(request);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeRole(@Valid @RequestBody UserRoleRequest request) {
        userRoleService.removeRole(request);
        return ResponseEntity.noContent().build();
    }
}

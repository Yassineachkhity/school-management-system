package org.openeye.authservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.authservice.dtos.RoleCreateRequest;
import org.openeye.authservice.dtos.RoleDTO;
import org.openeye.authservice.enums.RoleLabel;
import org.openeye.authservice.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{roleId}")
    public RoleDTO getRoleById(@PathVariable String roleId) {
        return roleService.getRoleById(roleId);
    }

    @GetMapping("/by-label")
    public RoleDTO getRoleByLabel(@RequestParam RoleLabel label) {
        return roleService.getRoleByLabel(label);
    }

    @GetMapping("/search")
    public List<RoleDTO> searchRoles(@RequestParam @NotBlank String keyword) {
        return roleService.searchRoles(keyword);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleDTO created = roleService.createRole(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{roleId}")
                .buildAndExpand(created.getRoleId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}

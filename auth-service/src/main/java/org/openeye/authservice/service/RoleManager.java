package org.openeye.authservice.service;

import lombok.RequiredArgsConstructor;
import org.openeye.authservice.dao.entities.Role;
import org.openeye.authservice.dao.repositories.RoleRepository;
import org.openeye.authservice.dao.repositories.UserRoleRepository;
import org.openeye.authservice.dtos.RoleCreateRequest;
import org.openeye.authservice.dtos.RoleDTO;
import org.openeye.authservice.enums.RoleLabel;
import org.openeye.authservice.exceptions.DuplicateRoleException;
import org.openeye.authservice.exceptions.RoleInUseException;
import org.openeye.authservice.exceptions.RoleNotFoundException;
import org.openeye.authservice.mappers.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleManager implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;
    private final KeycloakAdminService keycloakAdminService;

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleById(String roleId) {
        return roleMapper.toDTO(findRoleById(roleId));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleByLabel(RoleLabel label) {
        Role role = roleRepository.findByLabel(label);
        if (role == null) {
            throw new RoleNotFoundException("Role not found: " + label);
        }
        return roleMapper.toDTO(role);
    }

    @Override
    public RoleDTO createRole(RoleCreateRequest request) {
        RoleLabel label = request.getLabel();
        if (roleRepository.existsByLabel(label)) {
            throw new DuplicateRoleException("Role already exists: " + label);
        }
        Role role = Role.builder()
                .roleId(UUID.randomUUID().toString())
                .label(label)
                .build();
        Role saved = roleRepository.save(role);
        try {
            keycloakAdminService.ensureRealmRoleExists(label.name());
            return roleMapper.toDTO(saved);
        } catch (RuntimeException ex) {
            roleRepository.delete(saved);
            throw ex;
        }
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = findRoleById(roleId);
        if (!userRoleRepository.findByRoleId(roleId).isEmpty()) {
            throw new RoleInUseException("Role is assigned to users: " + role.getLabel());
        }
        keycloakAdminService.deleteRealmRole(role.getLabel().name());
        roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> searchRoles(String keyword) {
        String trimmedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        return roleRepository.findAll().stream()
                .filter(role -> role.getLabel().name().toLowerCase().contains(trimmedKeyword))
                .map(roleMapper::toDTO)
                .toList();
    }

    private Role findRoleById(String roleId) {
        String normalizedId = normalize(roleId);
        if (normalizedId == null) {
            throw new RoleNotFoundException("Role not found: " + roleId);
        }
        Role role = roleRepository.findByRoleId(normalizedId);
        if (role == null) {
            throw new RoleNotFoundException("Role not found: " + roleId);
        }
        return role;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

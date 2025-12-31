package org.openeye.authservice.service;

import lombok.RequiredArgsConstructor;
import org.openeye.authservice.dao.entities.Role;
import org.openeye.authservice.dao.entities.User;
import org.openeye.authservice.dao.entities.UserRole;
import org.openeye.authservice.dao.repositories.RoleRepository;
import org.openeye.authservice.dao.repositories.UserRepository;
import org.openeye.authservice.dao.repositories.UserRoleRepository;
import org.openeye.authservice.dtos.UserRoleDTO;
import org.openeye.authservice.dtos.UserRoleRequest;
import org.openeye.authservice.exceptions.RoleNotFoundException;
import org.openeye.authservice.exceptions.UserNotFoundException;
import org.openeye.authservice.exceptions.UserRoleAlreadyAssignedException;
import org.openeye.authservice.exceptions.UserRoleNotFoundException;
import org.openeye.authservice.mappers.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleManager implements UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;
    private final KeycloakAdminService keycloakAdminService;

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleRepository.findAll().stream()
                .map(userRole -> userRoleMapper.toDTO(userRole, roleRepository.findByRoleId(userRole.getRoleId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDTO> getRolesByUserId(String userId) {
        User user = findUserById(userId);
        return userRoleRepository.findByUserId(user.getUserId()).stream()
                .map(userRole -> userRoleMapper.toDTO(userRole, roleRepository.findByRoleId(userRole.getRoleId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDTO> getUsersByRoleId(String roleId) {
        Role role = findRoleById(roleId);
        return userRoleRepository.findByRoleId(role.getRoleId()).stream()
                .map(userRole -> userRoleMapper.toDTO(userRole, role))
                .toList();
    }

    @Override
    public UserRoleDTO assignRole(UserRoleRequest request) {
        User user = findUserById(request.getUserId());
        Role role = roleRepository.findByLabel(request.getRoleLabel());
        if (role == null) {
            throw new RoleNotFoundException("Role not found: " + request.getRoleLabel());
        }
        if (userRoleRepository.existsByUserIdAndRoleId(user.getUserId(), role.getRoleId())) {
            throw new UserRoleAlreadyAssignedException("Role already assigned to user");
        }

        keycloakAdminService.ensureRealmRoleExists(role.getLabel().name());
        UserRole userRole = UserRole.builder()
                .userId(user.getUserId())
                .roleId(role.getRoleId())
                .build();
        UserRole saved = userRoleRepository.save(userRole);
        keycloakAdminService.assignRealmRole(user.getUserId(), role.getLabel().name());
        return userRoleMapper.toDTO(saved, role);
    }

    @Override
    public void removeRole(UserRoleRequest request) {
        User user = findUserById(request.getUserId());
        Role role = roleRepository.findByLabel(request.getRoleLabel());
        if (role == null) {
            throw new RoleNotFoundException("Role not found: " + request.getRoleLabel());
        }
        if (!userRoleRepository.existsByUserIdAndRoleId(user.getUserId(), role.getRoleId())) {
            throw new UserRoleNotFoundException("Role not assigned to user");
        }
        userRoleRepository.deleteByUserIdAndRoleId(user.getUserId(), role.getRoleId());
        keycloakAdminService.removeRealmRole(user.getUserId(), role.getLabel().name());
    }

    private User findUserById(String userId) {
        String normalizedId = normalize(userId);
        if (normalizedId == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }
        User user = userRepository.findByUserId(normalizedId);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }
        return user;
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

package org.openeye.authservice.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.openeye.authservice.dao.entities.Role;
import org.openeye.authservice.dao.entities.User;
import org.openeye.authservice.dao.entities.UserRole;
import org.openeye.authservice.dao.repositories.RoleRepository;
import org.openeye.authservice.dao.repositories.UserRepository;
import org.openeye.authservice.dao.repositories.UserRoleRepository;
import org.openeye.authservice.dtos.UserCreateRequest;
import org.openeye.authservice.dtos.UserDTO;
import org.openeye.authservice.dtos.UserLastLoginUpdateRequest;
import org.openeye.authservice.dtos.UserPasswordUpdateRequest;
import org.openeye.authservice.dtos.UserUpdateRequest;
import org.openeye.authservice.enums.RoleLabel;
import org.openeye.authservice.exceptions.DuplicateEmailException;
import org.openeye.authservice.exceptions.UserNotFoundException;
import org.openeye.authservice.mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManager implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakAdminService keycloakAdminService;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(String userId) {
        return userMapper.toDTO(findUserById(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        String normalizedEmail = normalize(email);
        if (normalizedEmail == null) {
            throw new UserNotFoundException("User not found for email: " + email);
        }
        User user = userRepository.findByEmail(normalizedEmail);
        if (user == null) {
            throw new UserNotFoundException("User not found for email: " + email);
        }
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO createUser(UserCreateRequest request) {
        String email = normalize(request.getEmail());
        if (email == null) {
            throw new DuplicateEmailException("Email is required");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists: " + email);
        }

        RoleLabel roleLabel = request.getRoleLabel();
        Role role = roleRepository.findByLabel(roleLabel);
        if (role == null) {
            role = Role.builder()
                    .roleId(UUID.randomUUID().toString())
                    .label(roleLabel)
                    .build();
            roleRepository.save(role);
        }

        keycloakAdminService.ensureRealmRoleExists(roleLabel.name());
        String keycloakUserId = keycloakAdminService.createUser(email, request.getPassword());

        try {
            User user = User.builder()
                    .userId(keycloakUserId)
                    .email(email)
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .build();
            User saved = userRepository.save(user);

            UserRole userRole = UserRole.builder()
                    .userId(saved.getUserId())
                    .roleId(role.getRoleId())
                    .build();
            userRoleRepository.save(userRole);
            keycloakAdminService.assignRealmRole(saved.getUserId(), role.getLabel().name());

            return userMapper.toDTO(saved);
        } catch (RuntimeException ex) {
            keycloakAdminService.deleteUser(keycloakUserId);
            throw ex;
        }
    }

    @Override
    public UserDTO updateUser(String userId, UserUpdateRequest request) {
        User user = findUserById(userId);
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String email = normalize(request.getEmail());
            if (!email.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(email)) {
                throw new DuplicateEmailException("Email already exists: " + email);
            }
            keycloakAdminService.updateUserEmail(user.getUserId(), email);
            user.setEmail(email);
        }
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public void updatePassword(String userId, UserPasswordUpdateRequest request) {
        User user = findUserById(userId);
        String password = request.getPassword();
        keycloakAdminService.resetPassword(user.getUserId(), password);
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public UserDTO updateLastLogin(String userId, UserLastLoginUpdateRequest request) {
        User user = findUserById(userId);
        LocalDateTime lastLoginAt = request.getLastLoginAt();
        if (lastLoginAt == null) {
            lastLoginAt = LocalDateTime.now();
        }
        user.setLastLoginAt(lastLoginAt);
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public void deleteUser(String userId) {
        User user = findUserById(userId);
        keycloakAdminService.deleteUser(user.getUserId());
        userRoleRepository.deleteAll(userRoleRepository.findByUserId(user.getUserId()));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> searchUsers(String keyword) {
        String trimmedKeyword = keyword == null ? "" : keyword.trim();
        return userRepository.searchUsers(trimmedKeyword).stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public List<UserDTO> syncUsersFromKeycloak() {
        List<UserRepresentation> keycloakUsers = keycloakAdminService.listUsers();
        List<UserDTO> syncedUsers = new ArrayList<>();

        for (UserRepresentation keycloakUser : keycloakUsers) {
            String userId = normalize(keycloakUser.getId());
            String email = normalize(keycloakUser.getEmail());
            if (userId == null || email == null) {
                continue;
            }

            User user = userRepository.findByUserId(userId);
            if (user == null) {
                user = User.builder()
                        .userId(userId)
                        .email(email)
                        .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .build();
            } else {
                user.setEmail(email);
            }
            User saved = userRepository.save(user);
            syncUserRolesFromKeycloak(saved.getUserId());
            syncedUsers.add(userMapper.toDTO(saved));
        }

        return syncedUsers;
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

    private void syncUserRolesFromKeycloak(String userId) {
        List<String> roleNames = keycloakAdminService.getUserRealmRoles(userId);
        Set<RoleLabel> roleLabels = new HashSet<>();
        for (String roleName : roleNames) {
            if (roleName == null || roleName.isBlank()) {
                continue;
            }
            try {
                roleLabels.add(RoleLabel.valueOf(roleName.trim().toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                // Ignore non-domain roles.
            }
        }

        Map<RoleLabel, Role> roleByLabel = new EnumMap<>(RoleLabel.class);
        for (RoleLabel label : roleLabels) {
            Role role = roleRepository.findByLabel(label);
            if (role == null) {
                role = Role.builder()
                        .roleId(UUID.randomUUID().toString())
                        .label(label)
                        .build();
                role = roleRepository.save(role);
            }
            roleByLabel.put(label, role);
        }

        Set<String> desiredRoleIds = roleByLabel.values().stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet());

        List<UserRole> existingRoles = userRoleRepository.findByUserId(userId);
        for (UserRole existingRole : existingRoles) {
            if (!desiredRoleIds.contains(existingRole.getRoleId())) {
                userRoleRepository.delete(existingRole);
            }
        }

        for (Role role : roleByLabel.values()) {
            if (!userRoleRepository.existsByUserIdAndRoleId(userId, role.getRoleId())) {
                userRoleRepository.save(UserRole.builder()
                        .userId(userId)
                        .roleId(role.getRoleId())
                        .build());
            }
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

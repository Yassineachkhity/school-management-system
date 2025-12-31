package org.openeye.authservice.service;

import org.openeye.authservice.dtos.UserCreateRequest;
import org.openeye.authservice.dtos.UserDTO;
import org.openeye.authservice.dtos.UserLastLoginUpdateRequest;
import org.openeye.authservice.dtos.UserPasswordUpdateRequest;
import org.openeye.authservice.dtos.UserUpdateRequest;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(String userId);

    UserDTO getUserByEmail(String email);

    UserDTO createUser(UserCreateRequest request);

    UserDTO updateUser(String userId, UserUpdateRequest request);

    void updatePassword(String userId, UserPasswordUpdateRequest request);

    UserDTO updateLastLogin(String userId, UserLastLoginUpdateRequest request);

    void deleteUser(String userId);

    List<UserDTO> searchUsers(String keyword);

    List<UserDTO> syncUsersFromKeycloak();
}

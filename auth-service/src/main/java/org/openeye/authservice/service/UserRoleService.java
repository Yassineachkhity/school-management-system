package org.openeye.authservice.service;

import org.openeye.authservice.dtos.UserRoleDTO;
import org.openeye.authservice.dtos.UserRoleRequest;

import java.util.List;

public interface UserRoleService {
    List<UserRoleDTO> getAllUserRoles();

    List<UserRoleDTO> getRolesByUserId(String userId);

    List<UserRoleDTO> getUsersByRoleId(String roleId);

    UserRoleDTO assignRole(UserRoleRequest request);

    void removeRole(UserRoleRequest request);
}

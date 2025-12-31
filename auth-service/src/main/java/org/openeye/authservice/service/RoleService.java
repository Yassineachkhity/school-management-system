package org.openeye.authservice.service;

import org.openeye.authservice.dtos.RoleCreateRequest;
import org.openeye.authservice.dtos.RoleDTO;
import org.openeye.authservice.enums.RoleLabel;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();

    RoleDTO getRoleById(String roleId);

    RoleDTO getRoleByLabel(RoleLabel label);

    RoleDTO createRole(RoleCreateRequest request);

    void deleteRole(String roleId);

    List<RoleDTO> searchRoles(String keyword);
}

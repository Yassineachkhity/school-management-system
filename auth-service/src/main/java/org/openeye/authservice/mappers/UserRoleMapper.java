package org.openeye.authservice.mappers;

import org.openeye.authservice.dao.entities.Role;
import org.openeye.authservice.dao.entities.UserRole;
import org.openeye.authservice.dtos.UserRoleDTO;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper {

    public UserRoleDTO toDTO(UserRole userRole, Role role) {
        if (userRole == null) {
            return null;
        }
        return UserRoleDTO.builder()
                .userId(userRole.getUserId())
                .roleId(userRole.getRoleId())
                .roleLabel(role != null ? role.getLabel() : null)
                .build();
    }
}

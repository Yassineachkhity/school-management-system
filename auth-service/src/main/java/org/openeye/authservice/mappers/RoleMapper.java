package org.openeye.authservice.mappers;

import org.openeye.authservice.dao.entities.Role;
import org.openeye.authservice.dtos.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }
        return RoleDTO.builder()
                .roleId(role.getRoleId())
                .label(role.getLabel())
                .build();
    }
}

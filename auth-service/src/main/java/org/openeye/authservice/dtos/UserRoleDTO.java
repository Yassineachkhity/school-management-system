package org.openeye.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.authservice.enums.RoleLabel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {

    private String userId;
    private String roleId;
    private RoleLabel roleLabel;
}

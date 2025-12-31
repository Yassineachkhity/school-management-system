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
public class RoleDTO {

    private String roleId;
    private RoleLabel label;
}

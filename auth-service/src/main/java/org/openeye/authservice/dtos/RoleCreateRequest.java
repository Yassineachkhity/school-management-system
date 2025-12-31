package org.openeye.authservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.authservice.enums.RoleLabel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    @NotNull(message = "Role label is required")
    private RoleLabel label;
}

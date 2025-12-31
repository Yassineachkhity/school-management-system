package org.openeye.authservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.authservice.enums.RoleLabel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRequest {

    @NotBlank(message = "User ID is required")
    @Size(min = 1, max = 36, message = "User ID must be between 1 and 36 characters")
    private String userId;

    @NotNull(message = "Role label is required")
    private RoleLabel roleLabel;
}

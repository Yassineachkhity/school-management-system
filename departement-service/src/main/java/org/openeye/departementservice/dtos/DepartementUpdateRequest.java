package org.openeye.departementservice.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartementUpdateRequest {

    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    private String description;

    @Size(max = 20, message = "Head teacher ID must not exceed 20 characters")
    private String headTeacherId;

    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE, INACTIVE")
    private String status;
}

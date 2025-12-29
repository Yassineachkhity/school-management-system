package org.openeye.studentservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    private String address;

    @Min(value = 1, message = "Grade level must be at least 1")
    @Max(value = 12, message = "Grade level must not exceed 12")
    private Integer gradeLevel;

    @Size(max = 10, message = "Section must not exceed 10 characters")
    private String section;

    @Pattern(
            regexp = "ACTIVE|INACTIVE|GRADUATED|TRANSFERRED|SUSPENDED",
            message = "Status must be ACTIVE, INACTIVE, GRADUATED, TRANSFERRED, SUSPENDED"
    )
    private String status;
}

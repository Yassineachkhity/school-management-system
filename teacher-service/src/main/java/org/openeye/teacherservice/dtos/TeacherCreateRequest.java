package org.openeye.teacherservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCreateRequest {

    @NotBlank(message = "Employee number is required")
    @Size(min = 2, max = 20, message = "Employee number must be between 2 and 20 characters")
    private String employeeNumber;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    private String address;

    @NotBlank(message = "Departement is required")
    @Size(min = 1, max = 36, message = "Departement ID must be between 1 and 36 characters")
    private String departementId;

    @NotBlank(message = "Level is required")
    @Pattern(
            regexp = "ASSISTANT_PROFESSOR|FULL_TIME_PROFESSOR|PART_TIME_PROFESSOR",
            message = "Level must be ASSISTANT_PROFESSOR, FULL_TIME_PROFESSOR, PART_TIME_PROFESSOR"
    )
    private String level;

    @PastOrPresent(message = "Hire date must be in the past or present")
    private LocalDate hireDate;
}

package org.openeye.studentservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateRequest {

    @NotBlank(message = "User ID is required")
    @Size(min = 1, max = 36, message = "User ID must be between 1 and 36 characters")
    private String userId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @NotBlank(message = "Apoge code is required")
    @Size(min = 2, max = 20, message = "Apoge code must be between 2 and 20 characters")
    private String apogeCode;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotNull(message = "Admission date is required")
    @PastOrPresent(message = "Admission date must be in the past or present")
    private LocalDate admissionDate;

    @NotNull(message = "Grade level is required")
    @Min(value = 1, message = "Grade level must be at least 1")
    @Max(value = 5, message = "Grade level must not exceed 5")
    private Integer gradeLevel;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Departement is required")
    @Size(min = 1, max = 36, message = "Departement ID must be between 1 and 36 characters")
    private String departementId;
}

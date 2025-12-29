package org.openeye.inscriptionservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionCreateRequest {

    @NotBlank(message = "Student ID is required")
    @Size(min = 1, max = 36, message = "Student ID must be between 1 and 36 characters")
    private String studentId;

    @NotBlank(message = "Section ID is required")
    @Size(min = 1, max = 36, message = "Section ID must be between 1 and 36 characters")
    private String sectionId;

    @PastOrPresent(message = "Enrollment date must be in the past or present")
    private LocalDateTime enrolledAt;
}

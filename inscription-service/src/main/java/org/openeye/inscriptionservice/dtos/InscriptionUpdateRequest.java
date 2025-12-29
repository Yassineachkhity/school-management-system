package org.openeye.inscriptionservice.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "Grade must be at least 0.0")
    @DecimalMax(value = "20.0", inclusive = true, message = "Grade must not exceed 20.0")
    private Double grade;
}

package org.openeye.courseservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {

    @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters")
    private String title;

    private String description;

    @Min(value = 1, message = "Credit hours must be at least 1")
    private Integer creditHours;

    @Min(value = 1, message = "Semester must be at least 1")
    private Integer semester;

    @Size(min = 1, max = 36, message = "Departement ID must be between 1 and 36 characters")
    private String departementId;

    @Pattern(
            regexp = "ACTIVE|INACTIVE|ARCHIVED",
            message = "Status must be ACTIVE, INACTIVE, ARCHIVED"
    )
    private String status;
}

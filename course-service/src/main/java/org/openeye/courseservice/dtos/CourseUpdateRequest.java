package org.openeye.courseservice.dtos;

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
public class CourseUpdateRequest {

    @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters")
    private String title;

    private String description;

    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Min(value = 1, message = "Grade level must be at least 1")
    @Max(value = 12, message = "Grade level must not exceed 12")
    private Integer gradeLevel;

    @Min(value = 1, message = "Credit hours must be at least 1")
    @Max(value = 10, message = "Credit hours must not exceed 10")
    private Integer creditHours;

    @Size(max = 20, message = "Teacher ID must not exceed 20 characters")
    private String teacherId;

    @Pattern(
            regexp = "ACTIVE|INACTIVE|ARCHIVED",
            message = "Status must be ACTIVE, INACTIVE, ARCHIVED"
    )
    private String status;
}

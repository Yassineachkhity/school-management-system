package org.openeye.courseservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequest {

    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 150, message = "Title must be between 2 and 150 characters")
    private String title;

    private String description;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @NotNull(message = "Grade level is required")
    @Min(value = 1, message = "Grade level must be at least 1")
    @Max(value = 12, message = "Grade level must not exceed 12")
    private Integer gradeLevel;

    @NotNull(message = "Credit hours are required")
    @Min(value = 1, message = "Credit hours must be at least 1")
    @Max(value = 10, message = "Credit hours must not exceed 10")
    private Integer creditHours;

    @Size(max = 20, message = "Teacher ID must not exceed 20 characters")
    private String teacherId;
}

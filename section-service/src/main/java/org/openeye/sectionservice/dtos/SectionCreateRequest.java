package org.openeye.sectionservice.dtos;

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
public class SectionCreateRequest {

    @NotBlank(message = "Course ID is required")
    @Size(min = 1, max = 36, message = "Course ID must be between 1 and 36 characters")
    private String courseId;

    @NotBlank(message = "Teacher ID is required")
    @Size(min = 1, max = 36, message = "Teacher ID must be between 1 and 36 characters")
    private String teacherId;

    @NotBlank(message = "Academic year is required")
    @Size(min = 4, max = 20, message = "Academic year must be between 4 and 20 characters")
    private String academicYear;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotBlank(message = "Room is required")
    @Size(min = 1, max = 50, message = "Room must be between 1 and 50 characters")
    private String room;
}

package org.openeye.sectionservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionUpdateRequest {

    @Size(min = 1, max = 36, message = "Course ID must be between 1 and 36 characters")
    private String courseId;

    @Size(min = 1, max = 36, message = "Teacher ID must be between 1 and 36 characters")
    private String teacherId;

    @Size(min = 4, max = 20, message = "Academic year must be between 4 and 20 characters")
    private String academicYear;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @Size(min = 1, max = 50, message = "Room must be between 1 and 50 characters")
    private String room;
}

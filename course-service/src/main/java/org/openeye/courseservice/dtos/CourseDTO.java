package org.openeye.courseservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openeye.courseservice.enums.CourseStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long id;
    private String courseCode;

    private String title;
    private String description;
    private String department;

    private Integer gradeLevel;
    private Integer creditHours;
    private String teacherId;

    private CourseStatus status;
}

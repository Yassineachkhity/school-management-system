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

    private String courseId;
    private String courseCode;

    private String title;
    private String description;
    private String departementId;

    private Integer creditHours;
    private Integer semester;

    private CourseStatus status;
}

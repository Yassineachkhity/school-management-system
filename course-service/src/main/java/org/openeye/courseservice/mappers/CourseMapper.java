package org.openeye.courseservice.mappers;

import org.openeye.courseservice.dao.entities.Course;
import org.openeye.courseservice.dtos.CourseCreateRequest;
import org.openeye.courseservice.dtos.CourseDTO;
import org.openeye.courseservice.dtos.CourseUpdateRequest;
import org.openeye.courseservice.enums.CourseStatus;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        return CourseDTO.builder()
                .courseId(course.getCourseId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .creditHours(course.getCreditHours())
                .semester(course.getSemester())
                .departementId(course.getDepartementId())
                .status(course.getStatus())
                .build();
    }

    public Course toEntity(CourseCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creditHours(request.getCreditHours())
                .semester(request.getSemester())
                .departementId(request.getDepartementId())
                .status(CourseStatus.ACTIVE)
                .build();
    }

    public void updateEntityFromDTO(Course course, CourseUpdateRequest request) {
        if (course == null || request == null) {
            return;
        }

        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }
        if (request.getCreditHours() != null) {
            course.setCreditHours(request.getCreditHours());
        }
        if (request.getSemester() != null) {
            course.setSemester(request.getSemester());
        }
        if (request.getDepartementId() != null) {
            course.setDepartementId(request.getDepartementId());
        }
        if (request.getStatus() != null) {
            course.setStatus(parseStatus(request.getStatus()));
        }
    }

    private CourseStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return CourseStatus.valueOf(status.trim().toUpperCase());
    }
}

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
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .department(course.getDepartment())
                .gradeLevel(course.getGradeLevel())
                .creditHours(course.getCreditHours())
                .teacherId(course.getTeacherId())
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
                .department(request.getDepartment())
                .gradeLevel(request.getGradeLevel())
                .creditHours(request.getCreditHours())
                .teacherId(request.getTeacherId())
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
        if (request.getDepartment() != null) {
            course.setDepartment(request.getDepartment());
        }
        if (request.getGradeLevel() != null) {
            course.setGradeLevel(request.getGradeLevel());
        }
        if (request.getCreditHours() != null) {
            course.setCreditHours(request.getCreditHours());
        }
        if (request.getTeacherId() != null) {
            course.setTeacherId(request.getTeacherId());
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

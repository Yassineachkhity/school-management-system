package org.openeye.courseservice.service;

import org.openeye.courseservice.dtos.CourseCreateRequest;
import org.openeye.courseservice.dtos.CourseDTO;
import org.openeye.courseservice.dtos.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();

    CourseDTO getCourseByCourseCode(String courseCode);

    CourseDTO createCourse(CourseCreateRequest request);

    CourseDTO updateCourse(String courseCode, CourseUpdateRequest request);

    void deleteCourse(String courseCode);

    List<CourseDTO> searchCourses(String keyword);

    List<CourseDTO> getCoursesByDepartmentAndGradeLevel(String department, Integer gradeLevel);

    List<CourseDTO> getCoursesByTeacherId(String teacherId);

    List<CourseDTO> getActiveCoursesByDepartment(String department);

    long countActiveCoursesByDepartment(String department);
}

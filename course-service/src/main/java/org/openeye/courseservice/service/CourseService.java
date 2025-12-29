package org.openeye.courseservice.service;

import org.openeye.courseservice.dtos.CourseCreateRequest;
import org.openeye.courseservice.dtos.CourseDTO;
import org.openeye.courseservice.dtos.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();

    CourseDTO getCourseByCourseId(String courseId);

    CourseDTO createCourse(CourseCreateRequest request);

    CourseDTO updateCourse(String courseId, CourseUpdateRequest request);

    void deleteCourse(String courseId);

    List<CourseDTO> searchCourses(String keyword);

    List<CourseDTO> getCoursesByDepartementId(String departementId);

    List<CourseDTO> getCoursesByDepartementAndSemester(String departementId, Integer semester);

    List<CourseDTO> getCoursesBySemester(Integer semester);

    List<CourseDTO> getActiveCoursesByDepartement(String departementId);

    long countActiveCoursesByDepartement(String departementId);
}

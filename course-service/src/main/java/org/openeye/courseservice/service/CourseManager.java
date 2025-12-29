package org.openeye.courseservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.openeye.courseservice.clients.DepartementClient;
import org.openeye.courseservice.dao.entities.Course;
import org.openeye.courseservice.dao.repositories.CourseRepository;
import org.openeye.courseservice.dtos.CourseCreateRequest;
import org.openeye.courseservice.dtos.CourseDTO;
import org.openeye.courseservice.dtos.CourseUpdateRequest;
import org.openeye.courseservice.exceptions.CourseNotFoundException;
import org.openeye.courseservice.exceptions.DuplicateCourseException;
import org.openeye.courseservice.exceptions.InvalidDepartementException;
import org.openeye.courseservice.mappers.CourseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseManager implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final DepartementClient departementClient;

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseByCourseId(String courseId) {
        return courseMapper.toDTO(findCourseByCourseId(courseId));
    }

    @Override
    public CourseDTO createCourse(CourseCreateRequest request) {
        String courseCode = normalizeCourseCode(request.getCourseCode());
        if (courseRepository.existsByCourseCode(courseCode)) {
            throw new DuplicateCourseException("Course code already in use: " + courseCode);
        }

        String departementId = normalize(request.getDepartementId());
        validateDepartementExists(departementId);

        Course course = courseMapper.toEntity(request);
        course.setCourseId(UUID.randomUUID().toString());
        course.setCourseCode(courseCode);
        course.setDepartementId(departementId);

        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    @Override
    public CourseDTO updateCourse(String courseId, CourseUpdateRequest request) {
        Course course = findCourseByCourseId(courseId);

        String departementId = null;
        if (request.getDepartementId() != null) {
            departementId = normalize(request.getDepartementId());
            validateDepartementExists(departementId);
        }

        courseMapper.updateEntityFromDTO(course, request);
        if (departementId != null) {
            course.setDepartementId(departementId);
        }

        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    @Override
    public void deleteCourse(String courseId) {
        Course course = findCourseByCourseId(courseId);
        courseRepository.delete(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> searchCourses(String keyword) {
        String trimmedKeyword = keyword.trim();
        return courseRepository.searchCourses(trimmedKeyword).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByDepartementId(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return courseRepository.findByDepartementId(trimmedDepartementId).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByDepartementAndSemester(String departementId, Integer semester) {
        String trimmedDepartementId = normalize(departementId);
        return courseRepository.findByDepartementIdAndSemester(trimmedDepartementId, semester).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesBySemester(Integer semester) {
        return courseRepository.findBySemester(semester).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getActiveCoursesByDepartement(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return courseRepository.findActiveCoursesByDepartement(trimmedDepartementId).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveCoursesByDepartement(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return courseRepository.countActiveCoursesByDepartement(trimmedDepartementId);
    }

    private Course findCourseByCourseId(String courseId) {
        String normalizedId = normalize(courseId);
        if (normalizedId == null) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        Course course = courseRepository.findByCourseId(normalizedId);
        if (course == null) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        return course;
    }

    private String normalizeCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            return null;
        }
        return courseCode.trim().toUpperCase();
    }

    private void validateDepartementExists(String departementId) {
        if (departementId == null || departementId.isBlank()) {
            throw new InvalidDepartementException("Departement is required");
        }
        try {
            departementClient.getDepartementById(departementId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidDepartementException("Departement not found: " + departementId);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}

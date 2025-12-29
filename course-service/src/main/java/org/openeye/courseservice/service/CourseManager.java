package org.openeye.courseservice.service;

import lombok.RequiredArgsConstructor;
import org.openeye.courseservice.dao.entities.Course;
import org.openeye.courseservice.dao.repositories.CourseRepository;
import org.openeye.courseservice.dtos.CourseCreateRequest;
import org.openeye.courseservice.dtos.CourseDTO;
import org.openeye.courseservice.dtos.CourseUpdateRequest;
import org.openeye.courseservice.exceptions.CourseNotFoundException;
import org.openeye.courseservice.exceptions.DuplicateCourseException;
import org.openeye.courseservice.mappers.CourseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseManager implements CourseService {
    private static final int CODE_GENERATION_ATTEMPTS = 5;

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final SecureRandom random = new SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseByCourseCode(String courseCode) {
        return courseMapper.toDTO(findCourseByCourseCode(courseCode));
    }

    @Override
    public CourseDTO createCourse(CourseCreateRequest request) {
        String courseCode = normalizeCourseCode(request.getCourseCode());
        if (courseCode != null && courseRepository.existsByCourseCode(courseCode)) {
            throw new DuplicateCourseException("Course code already in use: " + courseCode);
        }
        if (courseCode == null) {
            courseCode = generateCourseCode();
        }

        Course course = courseMapper.toEntity(request);
        course.setCourseCode(courseCode);

        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    @Override
    public CourseDTO updateCourse(String courseCode, CourseUpdateRequest request) {
        Course course = findCourseByCourseCode(courseCode);
        courseMapper.updateEntityFromDTO(course, request);
        Course saved = courseRepository.save(course);
        return courseMapper.toDTO(saved);
    }

    @Override
    public void deleteCourse(String courseCode) {
        Course course = findCourseByCourseCode(courseCode);
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
    public List<CourseDTO> getCoursesByDepartmentAndGradeLevel(String department, Integer gradeLevel) {
        String trimmedDepartment = department.trim();
        return courseRepository.findByDepartmentAndGradeLevel(trimmedDepartment, gradeLevel).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByTeacherId(String teacherId) {
        String trimmedTeacherId = teacherId.trim();
        return courseRepository.findByTeacherId(trimmedTeacherId).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getActiveCoursesByDepartment(String department) {
        String trimmedDepartment = department.trim();
        return courseRepository.findActiveCoursesByDepartment(trimmedDepartment).stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveCoursesByDepartment(String department) {
        String trimmedDepartment = department.trim();
        return courseRepository.countActiveCoursesByDepartment(trimmedDepartment);
    }

    private Course findCourseByCourseCode(String courseCode) {
        String normalizedCode = normalizeCourseCode(courseCode);
        if (normalizedCode == null) {
            throw new CourseNotFoundException("Course not found: " + courseCode);
        }
        Course course = courseRepository.findByCourseCode(normalizedCode);
        if (course == null) {
            throw new CourseNotFoundException("Course not found: " + courseCode);
        }
        return course;
    }

    private String normalizeCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            return null;
        }
        return courseCode.trim().toUpperCase();
    }

    private String generateCourseCode() {
        String year = String.valueOf(LocalDate.now().getYear());
        for (int attempt = 0; attempt < CODE_GENERATION_ATTEMPTS; attempt++) {
            String candidate = "CRS" + year + String.format("%06d", random.nextInt(1_000_000));
            if (!courseRepository.existsByCourseCode(candidate)) {
                return candidate;
            }
        }

        String candidate;
        do {
            candidate = "CRS" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();
        } while (courseRepository.existsByCourseCode(candidate));
        return candidate;
    }
}

package org.openeye.courseservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.courseservice.dtos.CourseCreateRequest;
import org.openeye.courseservice.dtos.CourseDTO;
import org.openeye.courseservice.dtos.CourseUpdateRequest;
import org.openeye.courseservice.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseCode}")
    public CourseDTO getCourseByCourseCode(@PathVariable String courseCode) {
        return courseService.getCourseByCourseCode(courseCode);
    }

    @GetMapping("/by-department-grade")
    public List<CourseDTO> getCoursesByDepartmentAndGradeLevel(
            @RequestParam @NotBlank String department,
            @RequestParam @Min(1) @Max(12) Integer gradeLevel
    ) {
        return courseService.getCoursesByDepartmentAndGradeLevel(department, gradeLevel);
    }

    @GetMapping("/by-teacher")
    public List<CourseDTO> getCoursesByTeacherId(@RequestParam @NotBlank String teacherId) {
        return courseService.getCoursesByTeacherId(teacherId);
    }

    @GetMapping("/active")
    public List<CourseDTO> getActiveCoursesByDepartment(
            @RequestParam @NotBlank String department
    ) {
        return courseService.getActiveCoursesByDepartment(department);
    }

    @GetMapping("/active/count")
    public long countActiveCoursesByDepartment(
            @RequestParam @NotBlank String department
    ) {
        return courseService.countActiveCoursesByDepartment(department);
    }

    @GetMapping("/search")
    public List<CourseDTO> searchCourses(@RequestParam @NotBlank String keyword) {
        return courseService.searchCourses(keyword);
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseDTO created = courseService.createCourse(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{courseCode}")
                .buildAndExpand(created.getCourseCode())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{courseCode}")
    public CourseDTO updateCourse(
            @PathVariable String courseCode,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        return courseService.updateCourse(courseCode, request);
    }

    @DeleteMapping("/{courseCode}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return ResponseEntity.noContent().build();
    }
}

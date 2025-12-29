package org.openeye.courseservice.web;

import jakarta.validation.Valid;
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

    @GetMapping("/{courseId}")
    public CourseDTO getCourseByCourseId(@PathVariable String courseId) {
        return courseService.getCourseByCourseId(courseId);
    }

    @GetMapping("/by-departement")
    public List<CourseDTO> getCoursesByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return courseService.getCoursesByDepartementId(departementId);
    }

    @GetMapping("/by-departement-semester")
    public List<CourseDTO> getCoursesByDepartementAndSemester(
            @RequestParam @NotBlank String departementId,
            @RequestParam @Min(1) Integer semester
    ) {
        return courseService.getCoursesByDepartementAndSemester(departementId, semester);
    }

    @GetMapping("/by-semester")
    public List<CourseDTO> getCoursesBySemester(
            @RequestParam @Min(1) Integer semester
    ) {
        return courseService.getCoursesBySemester(semester);
    }

    @GetMapping("/active")
    public List<CourseDTO> getActiveCoursesByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return courseService.getActiveCoursesByDepartement(departementId);
    }

    @GetMapping("/active/count")
    public long countActiveCoursesByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return courseService.countActiveCoursesByDepartement(departementId);
    }

    @GetMapping("/search")
    public List<CourseDTO> searchCourses(@RequestParam @NotBlank String keyword) {
        return courseService.searchCourses(keyword);
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseDTO created = courseService.createCourse(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{courseId}")
                .buildAndExpand(created.getCourseId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{courseId}")
    public CourseDTO updateCourse(
            @PathVariable String courseId,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        return courseService.updateCourse(courseId, request);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}

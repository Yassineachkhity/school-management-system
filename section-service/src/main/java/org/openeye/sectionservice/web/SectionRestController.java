package org.openeye.sectionservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.sectionservice.dtos.SectionCreateRequest;
import org.openeye.sectionservice.dtos.SectionDTO;
import org.openeye.sectionservice.dtos.SectionUpdateRequest;
import org.openeye.sectionservice.service.SectionService;
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
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionRestController {

    private final SectionService sectionService;

    @GetMapping
    public List<SectionDTO> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{sectionId}")
    public SectionDTO getSectionById(@PathVariable String sectionId) {
        return sectionService.getSectionById(sectionId);
    }

    @GetMapping("/by-course")
    public List<SectionDTO> getSectionsByCourse(@RequestParam @NotBlank String courseId) {
        return sectionService.getSectionsByCourseId(courseId);
    }

    @GetMapping("/by-teacher")
    public List<SectionDTO> getSectionsByTeacher(@RequestParam @NotBlank String teacherId) {
        return sectionService.getSectionsByTeacherId(teacherId);
    }

    @GetMapping("/by-academic-year")
    public List<SectionDTO> getSectionsByAcademicYear(@RequestParam @NotBlank String academicYear) {
        return sectionService.getSectionsByAcademicYear(academicYear);
    }

    @GetMapping("/by-course-year")
    public List<SectionDTO> getSectionsByCourseAndYear(
            @RequestParam @NotBlank String courseId,
            @RequestParam @NotBlank String academicYear
    ) {
        return sectionService.getSectionsByCourseAndYear(courseId, academicYear);
    }

    @GetMapping("/search")
    public List<SectionDTO> searchSections(@RequestParam @NotBlank String keyword) {
        return sectionService.searchSections(keyword);
    }

    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@Valid @RequestBody SectionCreateRequest request) {
        SectionDTO created = sectionService.createSection(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{sectionId}")
                .buildAndExpand(created.getSectionId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{sectionId}")
    public SectionDTO updateSection(
            @PathVariable String sectionId,
            @Valid @RequestBody SectionUpdateRequest request
    ) {
        return sectionService.updateSection(sectionId, request);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable String sectionId) {
        sectionService.deleteSection(sectionId);
        return ResponseEntity.noContent().build();
    }
}

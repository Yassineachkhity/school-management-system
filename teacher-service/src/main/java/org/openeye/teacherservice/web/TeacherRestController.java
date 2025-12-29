package org.openeye.teacherservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.teacherservice.dtos.TeacherCreateRequest;
import org.openeye.teacherservice.dtos.TeacherDTO;
import org.openeye.teacherservice.dtos.TeacherUpdateRequest;
import org.openeye.teacherservice.enums.Level;
import org.openeye.teacherservice.service.TeacherService;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherRestController {

    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{teacherId}")
    public TeacherDTO getTeacherByTeacherId(@PathVariable String teacherId) {
        return teacherService.getTeacherByTeacherId(teacherId);
    }

    @GetMapping("/by-departement")
    public List<TeacherDTO> getTeachersByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return teacherService.getTeachersByDepartementId(departementId);
    }

    @GetMapping("/by-departement-level")
    public List<TeacherDTO> getTeachersByDepartementAndLevel(
            @RequestParam @NotBlank String departementId,
            @RequestParam Level level
    ) {
        return teacherService.getTeachersByDepartementAndLevel(departementId, level);
    }

    @GetMapping("/active")
    public List<TeacherDTO> getActiveTeachersByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return teacherService.getActiveTeachersByDepartement(departementId);
    }

    @GetMapping("/active/count")
    public long countActiveTeachersByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return teacherService.countActiveTeachersByDepartement(departementId);
    }

    @GetMapping("/search")
    public List<TeacherDTO> searchTeachers(@RequestParam @NotBlank String keyword) {
        return teacherService.searchTeachers(keyword);
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherCreateRequest request) {
        TeacherDTO created = teacherService.createTeacher(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{teacherId}")
                .buildAndExpand(created.getTeacherId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{teacherId}")
    public TeacherDTO updateTeacher(
            @PathVariable String teacherId,
            @Valid @RequestBody TeacherUpdateRequest request
    ) {
        return teacherService.updateTeacher(teacherId, request);
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.noContent().build();
    }
}

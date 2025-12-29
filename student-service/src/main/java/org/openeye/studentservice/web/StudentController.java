package org.openeye.studentservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.studentservice.dtos.StudentCreateRequest;
import org.openeye.studentservice.dtos.StudentDTO;
import org.openeye.studentservice.dtos.StudentUpdateRequest;
import org.openeye.studentservice.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    public StudentDTO getStudentByStudentId(@PathVariable String studentId) {
        return studentService.getStudentByStudentId(studentId);
    }

    @GetMapping("/by-departement")
    public List<StudentDTO> getStudentsByDepartement(
            @RequestParam @NotBlank String departementId
    ) {
        return studentService.getStudentsByDepartementId(departementId);
    }

    @GetMapping("/by-grade")
    public List<StudentDTO> getStudentsByGradeLevel(
            @RequestParam @Min(1) @Max(5) Integer gradeLevel
    ) {
        return studentService.getStudentsByGradeLevel(gradeLevel);
    }

    @GetMapping("/search")
    public List<StudentDTO> searchStudents(@RequestParam @NotBlank String keyword) {
        return studentService.searchStudents(keyword);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        StudentDTO created = studentService.createStudent(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studentId}")
                .buildAndExpand(created.getStudentId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{studentId}")
    public StudentDTO updateStudent(
            @PathVariable String studentId,
            @Valid @RequestBody StudentUpdateRequest request
    ) {
        return studentService.updateStudent(studentId, request);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}

package org.openeye.studentservice.service;

import lombok.RequiredArgsConstructor;
import org.openeye.studentservice.dao.entities.Student;
import org.openeye.studentservice.dao.repositories.StudentRepository;
import org.openeye.studentservice.dtos.StudentCreateRequest;
import org.openeye.studentservice.dtos.StudentDTO;
import org.openeye.studentservice.dtos.StudentUpdateRequest;
import org.openeye.studentservice.exceptions.DuplicateStudentException;
import org.openeye.studentservice.exceptions.StudentNotFoundException;
import org.openeye.studentservice.mappers.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentManager implements StudentService {
    private static final int ID_GENERATION_ATTEMPTS = 5;

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final SecureRandom random = new SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentByStudentId(String studentId) {
        return studentMapper.toDTO(findStudentByStudentId(studentId));
    }

    @Override
    public StudentDTO createStudent(StudentCreateRequest request) {
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateStudentException("Email already in use: " + request.getEmail());
        }

        Student student = studentMapper.toEntity(request);
        student.setStudentId(generateStudentId());
        student.setAdmissionDate(LocalDate.now());

        Student saved = studentRepository.save(student);
        return studentMapper.toDTO(saved);
    }

    @Override
    public StudentDTO updateStudent(String studentId, StudentUpdateRequest request) {
        Student student = findStudentByStudentId(studentId);

        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !request.getEmail().equalsIgnoreCase(student.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateStudentException("Email already in use: " + request.getEmail());
        }

        studentMapper.updateEntityFromDTO(student, request);
        Student saved = studentRepository.save(student);
        return studentMapper.toDTO(saved);
    }

    @Override
    public void deleteStudent(String studentId) {
        Student student = findStudentByStudentId(studentId);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> searchStudents(String keyword) {
        String trimmedKeyword = keyword.trim();
        return studentRepository.searchStudents(trimmedKeyword).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByGradeAndSection(Integer gradeLevel, String section) {
        String trimmedSection = section.trim();
        return studentRepository.findByGradeLevelAndSection(gradeLevel, trimmedSection).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getActiveStudentsByGrade(Integer gradeLevel) {
        return studentRepository.findActiveStudentsByGrade(gradeLevel).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveStudentsByGrade(Integer gradeLevel) {
        return studentRepository.countActiveStudentsByGrade(gradeLevel);
    }

    private Student findStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        return student;
    }

    private String generateStudentId() {
        String year = String.valueOf(LocalDate.now().getYear());
        for (int attempt = 0; attempt < ID_GENERATION_ATTEMPTS; attempt++) {
            String candidate = "STU" + year + String.format("%06d", random.nextInt(1_000_000));
            if (!studentRepository.existsByStudentId(candidate)) {
                return candidate;
            }
        }

        String candidate;
        do {
            candidate = "STU" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();
        } while (studentRepository.existsByStudentId(candidate));
        return candidate;
    }
}

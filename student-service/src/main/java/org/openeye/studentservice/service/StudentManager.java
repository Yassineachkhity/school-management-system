package org.openeye.studentservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.openeye.studentservice.clients.DepartementClient;
import org.openeye.studentservice.dao.entities.Student;
import org.openeye.studentservice.dao.repositories.StudentRepository;
import org.openeye.studentservice.dtos.StudentCreateRequest;
import org.openeye.studentservice.dtos.StudentDTO;
import org.openeye.studentservice.dtos.StudentUpdateRequest;
import org.openeye.studentservice.exceptions.DuplicateStudentException;
import org.openeye.studentservice.exceptions.InvalidDepartementException;
import org.openeye.studentservice.exceptions.StudentNotFoundException;
import org.openeye.studentservice.mappers.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentManager implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final DepartementClient departementClient;

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
        String apogeCode = normalize(request.getApogeCode());
        if (apogeCode != null && studentRepository.existsByApogeCode(apogeCode)) {
            throw new DuplicateStudentException("Apoge code already in use: " + apogeCode);
        }

        String departementId = normalize(request.getDepartementId());
        validateDepartementExists(departementId);

        Student student = studentMapper.toEntity(request);
        student.setStudentId(UUID.randomUUID().toString());
        student.setApogeCode(apogeCode);
        student.setEmail(normalize(request.getEmail()));
        student.setPhone(normalize(request.getPhone()));
        student.setDepartementId(departementId);

        Student saved = studentRepository.save(student);
        return studentMapper.toDTO(saved);
    }

    @Override
    public StudentDTO updateStudent(String studentId, StudentUpdateRequest request) {
        Student student = findStudentByStudentId(studentId);

        String apogeCode = null;
        if (request.getApogeCode() != null) {
            apogeCode = normalize(request.getApogeCode());
            if (!apogeCode.equalsIgnoreCase(student.getApogeCode())
                    && studentRepository.existsByApogeCode(apogeCode)) {
                throw new DuplicateStudentException("Apoge code already in use: " + apogeCode);
            }
        }

        String departementId = null;
        if (request.getDepartementId() != null) {
            departementId = normalize(request.getDepartementId());
            validateDepartementExists(departementId);
        }

        studentMapper.updateEntityFromDTO(student, request);

        if (apogeCode != null) {
            student.setApogeCode(apogeCode);
        }
        if (departementId != null) {
            student.setDepartementId(departementId);
        }
        if (request.getEmail() != null) {
            student.setEmail(normalize(request.getEmail()));
        }
        if (request.getPhone() != null) {
            student.setPhone(normalize(request.getPhone()));
        }
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
    public List<StudentDTO> getStudentsByDepartementId(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return studentRepository.findByDepartementId(trimmedDepartementId).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByGradeLevel(Integer gradeLevel) {
        return studentRepository.findByGradeLevel(gradeLevel).stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    private Student findStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        return student;
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

package org.openeye.studentservice.mappers;

import org.openeye.studentservice.dao.entities.Student;
import org.openeye.studentservice.dtos.StudentCreateRequest;
import org.openeye.studentservice.dtos.StudentDTO;
import org.openeye.studentservice.dtos.StudentUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }

        return StudentDTO.builder()
                .studentId(student.getStudentId())
                .userId(student.getUserId())
                .apogeCode(student.getApogeCode())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .admissionDate(student.getAdmissionDate())
                .gradeLevel(student.getGradeLevel())
                .dateOfBirth(student.getDateOfBirth())
                .departementId(student.getDepartementId())
                .build();
    }

    public Student toEntity(StudentCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Student.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .apogeCode(request.getApogeCode())
                .email(request.getEmail())
                .phone(request.getPhone())
                .admissionDate(request.getAdmissionDate())
                .gradeLevel(request.getGradeLevel())
                .dateOfBirth(request.getDateOfBirth())
                .departementId(request.getDepartementId())
                .build();
    }

    public void updateEntityFromDTO(Student student, StudentUpdateRequest request) {
        if (student == null || request == null) {
            return;
        }

        if (request.getFirstName() != null) {
            student.setFirstName(request.getFirstName());
        }
        if (request.getUserId() != null) {
            student.setUserId(request.getUserId());
        }
        if (request.getLastName() != null) {
            student.setLastName(request.getLastName());
        }
        if (request.getApogeCode() != null) {
            student.setApogeCode(request.getApogeCode());
        }
        if (request.getEmail() != null) {
            student.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            student.setPhone(request.getPhone());
        }
        if (request.getAdmissionDate() != null) {
            student.setAdmissionDate(request.getAdmissionDate());
        }
        if (request.getGradeLevel() != null) {
            student.setGradeLevel(request.getGradeLevel());
        }
        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getDepartementId() != null) {
            student.setDepartementId(request.getDepartementId());
        }
    }
}

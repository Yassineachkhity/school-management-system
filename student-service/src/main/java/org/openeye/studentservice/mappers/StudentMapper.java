package org.openeye.studentservice.mappers;

import org.openeye.studentservice.dao.entities.Student;
import org.openeye.studentservice.dtos.StudentCreateRequest;
import org.openeye.studentservice.dtos.StudentDTO;
import org.openeye.studentservice.dtos.StudentUpdateRequest;
import org.openeye.studentservice.enums.Gender;
import org.openeye.studentservice.enums.StudentStatus;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) return null;

        return StudentDTO.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .email(student.getEmail())
                .phone(student.getPhone())
                .address(student.getAddress())
                .admissionDate(student.getAdmissionDate())
                .gradeLevel(student.getGradeLevel())
                .section(student.getSection())
                .status(student.getStatus())
                .build();
    }

    public Student toEntity(StudentCreateRequest request) {
        if (request == null) return null;

        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(parseGender(request.getGender()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .gradeLevel(request.getGradeLevel())
                .section(request.getSection())
                .status(StudentStatus.ACTIVE)
                .build();
    }

    public void updateEntityFromDTO(Student student, StudentUpdateRequest request) {
        if (student == null || request == null) return;

        if (request.getFirstName() != null) student.setFirstName(request.getFirstName());
        if (request.getLastName() != null) student.setLastName(request.getLastName());
        if (request.getEmail() != null) student.setEmail(request.getEmail());
        if (request.getPhone() != null) student.setPhone(request.getPhone());
        if (request.getAddress() != null) student.setAddress(request.getAddress());
        if (request.getGradeLevel() != null) student.setGradeLevel(request.getGradeLevel());
        if (request.getSection() != null) student.setSection(request.getSection());

        if (request.getStatus() != null) {
            student.setStatus(parseStatus(request.getStatus()));
        }
    }

    private Gender parseGender(String gender) {
        if (gender == null || gender.isBlank()) return null;
        return Gender.valueOf(gender.trim().toUpperCase());
    }

    private StudentStatus parseStatus(String status) {
        if (status == null || status.isBlank()) return null;
        return StudentStatus.valueOf(status.trim().toUpperCase());
    }
}

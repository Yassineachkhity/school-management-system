package org.openeye.teacherservice.mappers;

import org.openeye.teacherservice.dao.entities.Teacher;
import org.openeye.teacherservice.dtos.TeacherCreateRequest;
import org.openeye.teacherservice.dtos.TeacherDTO;
import org.openeye.teacherservice.dtos.TeacherUpdateRequest;
import org.openeye.teacherservice.enums.Level;
import org.openeye.teacherservice.enums.TeacherStatus;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherDTO toDTO(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        return TeacherDTO.builder()
                .teacherId(teacher.getTeacherId())
                .userId(teacher.getUserId())
                .employeeNumber(teacher.getEmployeeNumber())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .address(teacher.getAddress())
                .departementId(teacher.getDepartementId())
                .level(teacher.getLevel())
                .status(teacher.getStatus())
                .hireDate(teacher.getHireDate())
                .build();
    }

    public Teacher toEntity(TeacherCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Teacher.builder()
                .userId(request.getUserId())
                .employeeNumber(request.getEmployeeNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .departementId(request.getDepartementId())
                .level(parseLevel(request.getLevel()))
                .hireDate(request.getHireDate())
                .status(TeacherStatus.ACTIVE)
                .build();
    }

    public void updateEntityFromDTO(Teacher teacher, TeacherUpdateRequest request) {
        if (teacher == null || request == null) {
            return;
        }

        if (request.getFirstName() != null) {
            teacher.setFirstName(request.getFirstName());
        }
        if (request.getUserId() != null) {
            teacher.setUserId(request.getUserId());
        }
        if (request.getLastName() != null) {
            teacher.setLastName(request.getLastName());
        }
        if (request.getEmployeeNumber() != null) {
            teacher.setEmployeeNumber(request.getEmployeeNumber());
        }
        if (request.getEmail() != null) {
            teacher.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            teacher.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            teacher.setAddress(request.getAddress());
        }
        if (request.getHireDate() != null) {
            teacher.setHireDate(request.getHireDate());
        }
        if (request.getDepartementId() != null) {
            teacher.setDepartementId(request.getDepartementId());
        }
        if (request.getLevel() != null) {
            teacher.setLevel(parseLevel(request.getLevel()));
        }
        if (request.getStatus() != null) {
            teacher.setStatus(parseStatus(request.getStatus()));
        }
    }

    private Level parseLevel(String level) {
        if (level == null || level.isBlank()) {
            return null;
        }
        return Level.valueOf(level.trim().toUpperCase());
    }

    private TeacherStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return TeacherStatus.valueOf(status.trim().toUpperCase());
    }
}

package org.openeye.teacherservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.openeye.teacherservice.clients.DepartementClient;
import org.openeye.teacherservice.dao.entities.Teacher;
import org.openeye.teacherservice.dao.repositories.TeacherRepository;
import org.openeye.teacherservice.dtos.TeacherCreateRequest;
import org.openeye.teacherservice.dtos.TeacherDTO;
import org.openeye.teacherservice.dtos.TeacherUpdateRequest;
import org.openeye.teacherservice.enums.Level;
import org.openeye.teacherservice.exceptions.DuplicateTeacherException;
import org.openeye.teacherservice.exceptions.InvalidDepartementException;
import org.openeye.teacherservice.exceptions.TeacherNotFoundException;
import org.openeye.teacherservice.mappers.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherManager implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final DepartementClient departementClient;

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDTO getTeacherByTeacherId(String teacherId) {
        return teacherMapper.toDTO(findTeacherByTeacherId(teacherId));
    }

    @Override
    public TeacherDTO createTeacher(TeacherCreateRequest request) {
        String employeeNumber = normalize(request.getEmployeeNumber());
        if (employeeNumber != null && teacherRepository.existsByEmployeeNumber(employeeNumber)) {
            throw new DuplicateTeacherException("Employee number already in use: " + employeeNumber);
        }

        String departementId = normalize(request.getDepartementId());
        validateDepartementExists(departementId);

        Teacher teacher = teacherMapper.toEntity(request);
        teacher.setTeacherId(UUID.randomUUID().toString());
        teacher.setEmployeeNumber(employeeNumber);
        teacher.setDepartementId(departementId);
        teacher.setEmail(normalize(request.getEmail()));
        teacher.setPhone(normalize(request.getPhone()));
        if (teacher.getHireDate() == null) {
            teacher.setHireDate(LocalDate.now());
        }

        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDTO(saved);
    }

    @Override
    public TeacherDTO updateTeacher(String teacherId, TeacherUpdateRequest request) {
        Teacher teacher = findTeacherByTeacherId(teacherId);

        String employeeNumber = null;
        if (request.getEmployeeNumber() != null) {
            employeeNumber = normalize(request.getEmployeeNumber());
            if (!employeeNumber.equalsIgnoreCase(teacher.getEmployeeNumber())
                    && teacherRepository.existsByEmployeeNumber(employeeNumber)) {
                throw new DuplicateTeacherException("Employee number already in use: " + employeeNumber);
            }
        }

        String departementId = null;
        if (request.getDepartementId() != null) {
            departementId = normalize(request.getDepartementId());
            validateDepartementExists(departementId);
        }

        teacherMapper.updateEntityFromDTO(teacher, request);
        if (employeeNumber != null) {
            teacher.setEmployeeNumber(employeeNumber);
        }
        if (departementId != null) {
            teacher.setDepartementId(departementId);
        }
        if (request.getEmail() != null) {
            teacher.setEmail(normalize(request.getEmail()));
        }
        if (request.getPhone() != null) {
            teacher.setPhone(normalize(request.getPhone()));
        }
        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDTO(saved);
    }

    @Override
    public void deleteTeacher(String teacherId) {
        Teacher teacher = findTeacherByTeacherId(teacherId);
        teacherRepository.delete(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> searchTeachers(String keyword) {
        String trimmedKeyword = keyword.trim();
        return teacherRepository.searchTeachers(trimmedKeyword).stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> getTeachersByDepartementId(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return teacherRepository.findByDepartementId(trimmedDepartementId).stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> getTeachersByDepartementAndLevel(String departementId, Level level) {
        String trimmedDepartementId = normalize(departementId);
        return teacherRepository.findByDepartementIdAndLevel(trimmedDepartementId, level).stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> getActiveTeachersByDepartement(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return teacherRepository.findActiveTeachersByDepartement(trimmedDepartementId).stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveTeachersByDepartement(String departementId) {
        String trimmedDepartementId = normalize(departementId);
        return teacherRepository.countActiveTeachersByDepartement(trimmedDepartementId);
    }

    private Teacher findTeacherByTeacherId(String teacherId) {
        Teacher teacher = teacherRepository.findByTeacherId(teacherId);
        if (teacher == null) {
            throw new TeacherNotFoundException("Teacher not found: " + teacherId);
        }
        return teacher;
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

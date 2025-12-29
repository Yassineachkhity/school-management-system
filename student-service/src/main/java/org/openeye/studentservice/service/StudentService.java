package org.openeye.studentservice.service;

import org.openeye.studentservice.dtos.StudentCreateRequest;
import org.openeye.studentservice.dtos.StudentDTO;
import org.openeye.studentservice.dtos.StudentUpdateRequest;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudents();

    StudentDTO getStudentByStudentId(String studentId);

    StudentDTO createStudent(StudentCreateRequest request);

    StudentDTO updateStudent(String studentId, StudentUpdateRequest request);

    void deleteStudent(String studentId);

    List<StudentDTO> searchStudents(String keyword);

    List<StudentDTO> getStudentsByGradeAndSection(Integer gradeLevel, String section);

    List<StudentDTO> getActiveStudentsByGrade(Integer gradeLevel);

    long countActiveStudentsByGrade(Integer gradeLevel);
}

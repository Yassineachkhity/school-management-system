package org.openeye.teacherservice.service;

import org.openeye.teacherservice.dtos.TeacherCreateRequest;
import org.openeye.teacherservice.dtos.TeacherDTO;
import org.openeye.teacherservice.dtos.TeacherUpdateRequest;
import org.openeye.teacherservice.enums.Level;

import java.util.List;

public interface TeacherService {
    List<TeacherDTO> getAllTeachers();

    TeacherDTO getTeacherByTeacherId(String teacherId);

    TeacherDTO createTeacher(TeacherCreateRequest request);

    TeacherDTO updateTeacher(String teacherId, TeacherUpdateRequest request);

    void deleteTeacher(String teacherId);

    List<TeacherDTO> searchTeachers(String keyword);

    List<TeacherDTO> getTeachersByDepartementId(String departementId);

    List<TeacherDTO> getTeachersByDepartementAndLevel(String departementId, Level level);

    List<TeacherDTO> getActiveTeachersByDepartement(String departementId);

    long countActiveTeachersByDepartement(String departementId);
}

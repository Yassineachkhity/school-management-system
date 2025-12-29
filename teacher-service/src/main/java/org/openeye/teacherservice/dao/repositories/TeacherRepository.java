package org.openeye.teacherservice.dao.repositories;

import org.openeye.teacherservice.dao.entities.Teacher;
import org.openeye.teacherservice.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    List<Teacher> findByDepartementId(String departementId);

    List<Teacher> findByDepartementIdAndLevel(String departementId, Level level);

    @Query("SELECT t FROM Teacher t WHERE t.departementId = :departementId AND t.status = 'ACTIVE'")
    List<Teacher> findActiveTeachersByDepartement(@Param("departementId") String departementId);

    @Query("SELECT t FROM Teacher t WHERE " +
            "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.teacherId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.employeeNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.departementId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Teacher> searchTeachers(@Param("keyword") String keyword);

    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.departementId = :departementId AND t.status = 'ACTIVE'")
    long countActiveTeachersByDepartement(@Param("departementId") String departementId);

    boolean existsByEmployeeNumber(String employeeNumber);

    Teacher findByTeacherId(String teacherId);
}

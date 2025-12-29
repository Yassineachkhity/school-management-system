package org.openeye.studentservice.dao.repositories;

import org.openeye.studentservice.dao.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGradeLevelAndSection(Integer gradeLevel, String section);

    @Query("SELECT s FROM Student s WHERE s.gradeLevel = :grade AND s.status = 'ACTIVE'")
    List<Student> findActiveStudentsByGrade(@Param("grade") Integer grade);

    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchStudents(@Param("keyword") String keyword);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.gradeLevel = :grade AND s.status = 'ACTIVE'")
    long countActiveStudentsByGrade(@Param("grade") Integer grade);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    Student findByStudentId(String studentId);
}

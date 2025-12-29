package org.openeye.studentservice.dao.repositories;

import org.openeye.studentservice.dao.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByDepartementId(String departementId);

    List<Student> findByGradeLevel(Integer gradeLevel);

    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.apogeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchStudents(@Param("keyword") String keyword);

    boolean existsByApogeCode(String apogeCode);

    Student findByStudentId(String studentId);
}

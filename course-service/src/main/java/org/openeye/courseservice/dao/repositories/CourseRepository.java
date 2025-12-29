package org.openeye.courseservice.dao.repositories;

import org.openeye.courseservice.dao.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDepartmentAndGradeLevel(String department, Integer gradeLevel);

    List<Course> findByTeacherId(String teacherId);

    @Query("SELECT c FROM Course c WHERE c.department = :department AND c.status = 'ACTIVE'")
    List<Course> findActiveCoursesByDepartment(@Param("department") String department);

    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.teacherId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCourses(@Param("keyword") String keyword);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.department = :department AND c.status = 'ACTIVE'")
    long countActiveCoursesByDepartment(@Param("department") String department);

    boolean existsByCourseCode(String courseCode);

    Course findByCourseCode(String courseCode);
}

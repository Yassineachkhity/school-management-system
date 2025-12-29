package org.openeye.courseservice.dao.repositories;

import org.openeye.courseservice.dao.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByDepartementId(String departementId);

    List<Course> findByDepartementIdAndSemester(String departementId, Integer semester);

    List<Course> findBySemester(Integer semester);

    @Query("SELECT c FROM Course c WHERE c.departementId = :departementId AND c.status = 'ACTIVE'")
    List<Course> findActiveCoursesByDepartement(@Param("departementId") String departementId);

    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.departementId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCourses(@Param("keyword") String keyword);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.departementId = :departementId AND c.status = 'ACTIVE'")
    long countActiveCoursesByDepartement(@Param("departementId") String departementId);

    boolean existsByCourseCode(String courseCode);

    Course findByCourseCode(String courseCode);

    Course findByCourseId(String courseId);
}

package org.openeye.sectionservice.dao.repositories;

import org.openeye.sectionservice.dao.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, String> {
    List<Section> findByCourseId(String courseId);

    List<Section> findByTeacherId(String teacherId);

    List<Section> findByAcademicYear(String academicYear);

    List<Section> findByCourseIdAndAcademicYear(String courseId, String academicYear);

    @Query("SELECT s FROM Section s WHERE " +
            "LOWER(s.courseId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.teacherId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.academicYear) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.room) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Section> searchSections(@Param("keyword") String keyword);

    Section findBySectionId(String sectionId);
}

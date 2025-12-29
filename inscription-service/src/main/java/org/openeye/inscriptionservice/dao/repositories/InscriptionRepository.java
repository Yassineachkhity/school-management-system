package org.openeye.inscriptionservice.dao.repositories;

import org.openeye.inscriptionservice.dao.entities.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, String> {
    List<Inscription> findByStudentId(String studentId);

    List<Inscription> findBySectionId(String sectionId);

    boolean existsByStudentIdAndSectionId(String studentId, String sectionId);

    @Query("SELECT i FROM Inscription i WHERE " +
            "LOWER(i.studentId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.sectionId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Inscription> searchInscriptions(@Param("keyword") String keyword);

    Inscription findByEnrollmentId(String enrollmentId);
}

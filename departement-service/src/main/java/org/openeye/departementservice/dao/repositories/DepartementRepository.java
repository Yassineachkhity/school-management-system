package org.openeye.departementservice.dao.repositories;

import org.openeye.departementservice.dao.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartementRepository extends JpaRepository<Departement, String> {
    @Query("SELECT d FROM Departement d WHERE " +
            "LOWER(d.departementCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Departement> searchDepartements(@Param("keyword") String keyword);

    boolean existsByDepartementCode(String departementCode);

    Departement findByDepartementCode(String departementCode);

    Departement findByDepartementId(String departementId);
}

package org.openeye.departementservice.service;

import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;

import java.util.List;

public interface DepartementService {
    List<DepartementDTO> getAllDepartements();

    DepartementDTO getDepartementByCode(String departementCode);

    DepartementDTO createDepartement(DepartementCreateRequest request);

    DepartementDTO updateDepartement(String departementCode, DepartementUpdateRequest request);

    void deleteDepartement(String departementCode);

    List<DepartementDTO> searchDepartements(String keyword);

    List<DepartementDTO> getActiveDepartements();

    long countActiveDepartements();
}

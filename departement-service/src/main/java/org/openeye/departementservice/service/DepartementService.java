package org.openeye.departementservice.service;

import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;

import java.util.List;

public interface DepartementService {
    List<DepartementDTO> getAllDepartements();

    DepartementDTO getDepartementById(String departementId);

    DepartementDTO getDepartementByCode(String departementCode);

    DepartementDTO createDepartement(DepartementCreateRequest request);

    DepartementDTO updateDepartement(String departementId, DepartementUpdateRequest request);

    void deleteDepartement(String departementId);

    List<DepartementDTO> searchDepartements(String keyword);
}

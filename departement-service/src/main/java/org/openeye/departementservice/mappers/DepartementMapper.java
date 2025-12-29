package org.openeye.departementservice.mappers;

import org.openeye.departementservice.dao.entities.Departement;
import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;
import org.openeye.departementservice.enums.DepartementStatus;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    public DepartementDTO toDTO(Departement departement) {
        if (departement == null) {
            return null;
        }

        return DepartementDTO.builder()
                .id(departement.getId())
                .departementCode(departement.getDepartementCode())
                .name(departement.getName())
                .description(departement.getDescription())
                .headTeacherId(departement.getHeadTeacherId())
                .status(departement.getStatus())
                .build();
    }

    public Departement toEntity(DepartementCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Departement.builder()
                .name(request.getName())
                .description(request.getDescription())
                .headTeacherId(request.getHeadTeacherId())
                .status(DepartementStatus.ACTIVE)
                .build();
    }

    public void updateEntityFromDTO(Departement departement, DepartementUpdateRequest request) {
        if (departement == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            departement.setName(request.getName());
        }
        if (request.getDescription() != null) {
            departement.setDescription(request.getDescription());
        }
        if (request.getHeadTeacherId() != null) {
            departement.setHeadTeacherId(request.getHeadTeacherId());
        }
        if (request.getStatus() != null) {
            departement.setStatus(parseStatus(request.getStatus()));
        }
    }

    private DepartementStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return DepartementStatus.valueOf(status.trim().toUpperCase());
    }
}

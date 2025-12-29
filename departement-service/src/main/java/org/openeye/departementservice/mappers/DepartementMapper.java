package org.openeye.departementservice.mappers;

import org.openeye.departementservice.dao.entities.Departement;
import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    public DepartementDTO toDTO(Departement departement) {
        if (departement == null) {
            return null;
        }

        return DepartementDTO.builder()
                .departementId(departement.getDepartementId())
                .departementCode(departement.getDepartementCode())
                .name(departement.getName())
                .description(departement.getDescription())
                .headTeacherId(departement.getHeadTeacherId())
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
    }
}

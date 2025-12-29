package org.openeye.inscriptionservice.mappers;

import org.openeye.inscriptionservice.dao.entities.Inscription;
import org.openeye.inscriptionservice.dtos.InscriptionCreateRequest;
import org.openeye.inscriptionservice.dtos.InscriptionDTO;
import org.openeye.inscriptionservice.dtos.InscriptionUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class InscriptionMapper {

    public InscriptionDTO toDTO(Inscription inscription) {
        if (inscription == null) {
            return null;
        }

        return InscriptionDTO.builder()
                .enrollmentId(inscription.getEnrollmentId())
                .studentId(inscription.getStudentId())
                .sectionId(inscription.getSectionId())
                .enrolledAt(inscription.getEnrolledAt())
                .grade(inscription.getGrade())
                .build();
    }

    public Inscription toEntity(InscriptionCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Inscription.builder()
                .studentId(request.getStudentId())
                .sectionId(request.getSectionId())
                .enrolledAt(request.getEnrolledAt())
                .build();
    }

    public void updateEntityFromDTO(Inscription inscription, InscriptionUpdateRequest request) {
        if (inscription == null || request == null) {
            return;
        }

        if (request.getGrade() != null) {
            inscription.setGrade(request.getGrade());
        }
    }
}

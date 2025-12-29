package org.openeye.inscriptionservice.service;

import org.openeye.inscriptionservice.dtos.InscriptionCreateRequest;
import org.openeye.inscriptionservice.dtos.InscriptionDTO;
import org.openeye.inscriptionservice.dtos.InscriptionUpdateRequest;

import java.util.List;

public interface InscriptionService {
    List<InscriptionDTO> getAllInscriptions();

    InscriptionDTO getInscriptionById(String enrollmentId);

    InscriptionDTO createInscription(InscriptionCreateRequest request);

    InscriptionDTO updateInscription(String enrollmentId, InscriptionUpdateRequest request);

    void deleteInscription(String enrollmentId);

    List<InscriptionDTO> searchInscriptions(String keyword);

    List<InscriptionDTO> getInscriptionsByStudentId(String studentId);

    List<InscriptionDTO> getInscriptionsBySectionId(String sectionId);
}

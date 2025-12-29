package org.openeye.inscriptionservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.openeye.inscriptionservice.clients.SectionClient;
import org.openeye.inscriptionservice.clients.StudentClient;
import org.openeye.inscriptionservice.dao.entities.Inscription;
import org.openeye.inscriptionservice.dao.repositories.InscriptionRepository;
import org.openeye.inscriptionservice.dtos.InscriptionCreateRequest;
import org.openeye.inscriptionservice.dtos.InscriptionDTO;
import org.openeye.inscriptionservice.dtos.InscriptionUpdateRequest;
import org.openeye.inscriptionservice.exceptions.DuplicateInscriptionException;
import org.openeye.inscriptionservice.exceptions.InscriptionNotFoundException;
import org.openeye.inscriptionservice.exceptions.InvalidSectionException;
import org.openeye.inscriptionservice.exceptions.InvalidStudentException;
import org.openeye.inscriptionservice.mappers.InscriptionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionManager implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;
    private final StudentClient studentClient;
    private final SectionClient sectionClient;

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionDTO> getAllInscriptions() {
        return inscriptionRepository.findAll().stream()
                .map(inscriptionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InscriptionDTO getInscriptionById(String enrollmentId) {
        return inscriptionMapper.toDTO(findInscriptionById(enrollmentId));
    }

    @Override
    public InscriptionDTO createInscription(InscriptionCreateRequest request) {
        String studentId = normalize(request.getStudentId());
        String sectionId = normalize(request.getSectionId());
        validateStudentExists(studentId);
        validateSectionExists(sectionId);

        if (inscriptionRepository.existsByStudentIdAndSectionId(studentId, sectionId)) {
            throw new DuplicateInscriptionException("Student already enrolled in this section");
        }

        Inscription inscription = inscriptionMapper.toEntity(request);
        inscription.setEnrollmentId(UUID.randomUUID().toString());
        inscription.setStudentId(studentId);
        inscription.setSectionId(sectionId);
        if (inscription.getEnrolledAt() == null) {
            inscription.setEnrolledAt(LocalDateTime.now());
        }

        Inscription saved = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDTO(saved);
    }

    @Override
    public InscriptionDTO updateInscription(String enrollmentId, InscriptionUpdateRequest request) {
        Inscription inscription = findInscriptionById(enrollmentId);
        inscriptionMapper.updateEntityFromDTO(inscription, request);
        Inscription saved = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDTO(saved);
    }

    @Override
    public void deleteInscription(String enrollmentId) {
        Inscription inscription = findInscriptionById(enrollmentId);
        inscriptionRepository.delete(inscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionDTO> searchInscriptions(String keyword) {
        String trimmedKeyword = keyword.trim();
        return inscriptionRepository.searchInscriptions(trimmedKeyword).stream()
                .map(inscriptionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionDTO> getInscriptionsByStudentId(String studentId) {
        String trimmedStudentId = normalize(studentId);
        return inscriptionRepository.findByStudentId(trimmedStudentId).stream()
                .map(inscriptionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionDTO> getInscriptionsBySectionId(String sectionId) {
        String trimmedSectionId = normalize(sectionId);
        return inscriptionRepository.findBySectionId(trimmedSectionId).stream()
                .map(inscriptionMapper::toDTO)
                .toList();
    }

    private Inscription findInscriptionById(String enrollmentId) {
        String normalizedId = normalize(enrollmentId);
        if (normalizedId == null) {
            throw new InscriptionNotFoundException("Enrollment not found: " + enrollmentId);
        }
        Inscription inscription = inscriptionRepository.findByEnrollmentId(normalizedId);
        if (inscription == null) {
            throw new InscriptionNotFoundException("Enrollment not found: " + enrollmentId);
        }
        return inscription;
    }

    private void validateStudentExists(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new InvalidStudentException("Student is required");
        }
        try {
            studentClient.getStudentById(studentId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidStudentException("Student not found: " + studentId);
        }
    }

    private void validateSectionExists(String sectionId) {
        if (sectionId == null || sectionId.isBlank()) {
            throw new InvalidSectionException("Section is required");
        }
        try {
            sectionClient.getSectionById(sectionId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidSectionException("Section not found: " + sectionId);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}

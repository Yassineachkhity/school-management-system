package org.openeye.departementservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.openeye.departementservice.clients.TeacherClient;
import org.openeye.departementservice.dao.entities.Departement;
import org.openeye.departementservice.dao.repositories.DepartementRepository;
import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;
import org.openeye.departementservice.exceptions.DepartementNotFoundException;
import org.openeye.departementservice.exceptions.DuplicateDepartementException;
import org.openeye.departementservice.exceptions.InvalidTeacherException;
import org.openeye.departementservice.mappers.DepartementMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartementManager implements DepartementService {

    private final DepartementRepository departementRepository;
    private final DepartementMapper departementMapper;
    private final TeacherClient teacherClient;

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDTO> getAllDepartements() {
        return departementRepository.findAll().stream()
                .map(departementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartementDTO getDepartementById(String departementId) {
        return departementMapper.toDTO(findDepartementById(departementId));
    }

    @Override
    @Transactional(readOnly = true)
    public DepartementDTO getDepartementByCode(String departementCode) {
        return departementMapper.toDTO(findDepartementByCode(departementCode));
    }

    @Override
    public DepartementDTO createDepartement(DepartementCreateRequest request) {
        String departementCode = normalizeDepartementCode(request.getDepartementCode());
        if (departementRepository.existsByDepartementCode(departementCode)) {
            throw new DuplicateDepartementException("Departement code already in use: " + departementCode);
        }

        String headTeacherId = null;
        if (request.getHeadTeacherId() != null) {
            headTeacherId = normalize(request.getHeadTeacherId());
            validateTeacherExists(headTeacherId);
        }

        Departement departement = departementMapper.toEntity(request);
        departement.setDepartementId(UUID.randomUUID().toString());
        departement.setDepartementCode(departementCode);
        departement.setHeadTeacherId(headTeacherId);

        Departement saved = departementRepository.save(departement);
        return departementMapper.toDTO(saved);
    }

    @Override
    public DepartementDTO updateDepartement(String departementId, DepartementUpdateRequest request) {
        Departement departement = findDepartementById(departementId);

        String headTeacherId = null;
        if (request.getHeadTeacherId() != null) {
            headTeacherId = normalize(request.getHeadTeacherId());
            validateTeacherExists(headTeacherId);
        }

        departementMapper.updateEntityFromDTO(departement, request);
        if (headTeacherId != null) {
            departement.setHeadTeacherId(headTeacherId);
        }
        Departement saved = departementRepository.save(departement);
        return departementMapper.toDTO(saved);
    }

    @Override
    public void deleteDepartement(String departementId) {
        Departement departement = findDepartementById(departementId);
        departementRepository.delete(departement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDTO> searchDepartements(String keyword) {
        String trimmedKeyword = keyword.trim();
        return departementRepository.searchDepartements(trimmedKeyword).stream()
                .map(departementMapper::toDTO)
                .toList();
    }

    private Departement findDepartementById(String departementId) {
        String normalizedId = normalize(departementId);
        if (normalizedId == null) {
            throw new DepartementNotFoundException("Departement not found: " + departementId);
        }
        Departement departement = departementRepository.findByDepartementId(normalizedId);
        if (departement == null) {
            throw new DepartementNotFoundException("Departement not found: " + departementId);
        }
        return departement;
    }

    private String normalizeDepartementCode(String departementCode) {
        if (departementCode == null || departementCode.isBlank()) {
            return null;
        }
        return departementCode.trim().toUpperCase();
    }

    private Departement findDepartementByCode(String departementCode) {
        String normalizedCode = normalizeDepartementCode(departementCode);
        if (normalizedCode == null) {
            throw new DepartementNotFoundException("Departement not found: " + departementCode);
        }
        Departement departement = departementRepository.findByDepartementCode(normalizedCode);
        if (departement == null) {
            throw new DepartementNotFoundException("Departement not found: " + departementCode);
        }
        return departement;
    }

    private void validateTeacherExists(String teacherId) {
        if (teacherId == null || teacherId.isBlank()) {
            throw new InvalidTeacherException("Head teacher is required");
        }
        try {
            teacherClient.getTeacherById(teacherId);
        } catch (FeignException.NotFound ex) {
            throw new InvalidTeacherException("Teacher not found: " + teacherId);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}

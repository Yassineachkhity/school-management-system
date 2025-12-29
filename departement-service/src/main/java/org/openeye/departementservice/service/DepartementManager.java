package org.openeye.departementservice.service;

import lombok.RequiredArgsConstructor;
import org.openeye.departementservice.dao.entities.Departement;
import org.openeye.departementservice.dao.repositories.DepartementRepository;
import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;
import org.openeye.departementservice.exceptions.DepartementNotFoundException;
import org.openeye.departementservice.exceptions.DuplicateDepartementException;
import org.openeye.departementservice.mappers.DepartementMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartementManager implements DepartementService {
    private static final int CODE_GENERATION_ATTEMPTS = 5;

    private final DepartementRepository departementRepository;
    private final DepartementMapper departementMapper;
    private final SecureRandom random = new SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDTO> getAllDepartements() {
        return departementRepository.findAll().stream()
                .map(departementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartementDTO getDepartementByCode(String departementCode) {
        return departementMapper.toDTO(findDepartementByCode(departementCode));
    }

    @Override
    public DepartementDTO createDepartement(DepartementCreateRequest request) {
        String departementCode = normalizeDepartementCode(request.getDepartementCode());
        if (departementCode != null && departementRepository.existsByDepartementCode(departementCode)) {
            throw new DuplicateDepartementException("Departement code already in use: " + departementCode);
        }
        if (departementCode == null) {
            departementCode = generateDepartementCode();
        }

        Departement departement = departementMapper.toEntity(request);
        departement.setDepartementCode(departementCode);

        Departement saved = departementRepository.save(departement);
        return departementMapper.toDTO(saved);
    }

    @Override
    public DepartementDTO updateDepartement(String departementCode, DepartementUpdateRequest request) {
        Departement departement = findDepartementByCode(departementCode);
        departementMapper.updateEntityFromDTO(departement, request);
        Departement saved = departementRepository.save(departement);
        return departementMapper.toDTO(saved);
    }

    @Override
    public void deleteDepartement(String departementCode) {
        Departement departement = findDepartementByCode(departementCode);
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

    @Override
    @Transactional(readOnly = true)
    public List<DepartementDTO> getActiveDepartements() {
        return departementRepository.findActiveDepartements().stream()
                .map(departementMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveDepartements() {
        return departementRepository.countActiveDepartements();
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

    private String normalizeDepartementCode(String departementCode) {
        if (departementCode == null || departementCode.isBlank()) {
            return null;
        }
        return departementCode.trim().toUpperCase();
    }

    private String generateDepartementCode() {
        String year = String.valueOf(LocalDate.now().getYear());
        for (int attempt = 0; attempt < CODE_GENERATION_ATTEMPTS; attempt++) {
            String candidate = "DEP" + year + String.format("%04d", random.nextInt(10_000));
            if (!departementRepository.existsByDepartementCode(candidate)) {
                return candidate;
            }
        }

        String candidate;
        do {
            candidate = "DEP" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();
        } while (departementRepository.existsByDepartementCode(candidate));
        return candidate;
    }
}

package org.openeye.inscriptionservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.inscriptionservice.dtos.InscriptionCreateRequest;
import org.openeye.inscriptionservice.dtos.InscriptionDTO;
import org.openeye.inscriptionservice.dtos.InscriptionUpdateRequest;
import org.openeye.inscriptionservice.service.InscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionRestController {

    private final InscriptionService inscriptionService;

    @GetMapping
    public List<InscriptionDTO> getAllInscriptions() {
        return inscriptionService.getAllInscriptions();
    }

    @GetMapping("/{enrollmentId}")
    public InscriptionDTO getInscriptionById(@PathVariable String enrollmentId) {
        return inscriptionService.getInscriptionById(enrollmentId);
    }

    @GetMapping("/by-student")
    public List<InscriptionDTO> getInscriptionsByStudent(@RequestParam @NotBlank String studentId) {
        return inscriptionService.getInscriptionsByStudentId(studentId);
    }

    @GetMapping("/by-section")
    public List<InscriptionDTO> getInscriptionsBySection(@RequestParam @NotBlank String sectionId) {
        return inscriptionService.getInscriptionsBySectionId(sectionId);
    }

    @GetMapping("/search")
    public List<InscriptionDTO> searchInscriptions(@RequestParam @NotBlank String keyword) {
        return inscriptionService.searchInscriptions(keyword);
    }

    @PostMapping
    public ResponseEntity<InscriptionDTO> createInscription(
            @Valid @RequestBody InscriptionCreateRequest request
    ) {
        InscriptionDTO created = inscriptionService.createInscription(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{enrollmentId}")
                .buildAndExpand(created.getEnrollmentId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{enrollmentId}")
    public InscriptionDTO updateInscription(
            @PathVariable String enrollmentId,
            @Valid @RequestBody InscriptionUpdateRequest request
    ) {
        return inscriptionService.updateInscription(enrollmentId, request);
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteInscription(@PathVariable String enrollmentId) {
        inscriptionService.deleteInscription(enrollmentId);
        return ResponseEntity.noContent().build();
    }
}

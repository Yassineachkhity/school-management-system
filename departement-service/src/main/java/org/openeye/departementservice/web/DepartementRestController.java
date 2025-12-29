package org.openeye.departementservice.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.openeye.departementservice.dtos.DepartementCreateRequest;
import org.openeye.departementservice.dtos.DepartementDTO;
import org.openeye.departementservice.dtos.DepartementUpdateRequest;
import org.openeye.departementservice.service.DepartementService;
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
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementRestController {

    private final DepartementService departementService;

    @GetMapping
    public List<DepartementDTO> getAllDepartements() {
        return departementService.getAllDepartements();
    }

    @GetMapping("/{departementId}")
    public DepartementDTO getDepartementById(@PathVariable String departementId) {
        return departementService.getDepartementById(departementId);
    }

    @GetMapping("/by-code")
    public DepartementDTO getDepartementByCode(@RequestParam @NotBlank String code) {
        return departementService.getDepartementByCode(code);
    }

    @GetMapping("/search")
    public List<DepartementDTO> searchDepartements(@RequestParam @NotBlank String keyword) {
        return departementService.searchDepartements(keyword);
    }

    @PostMapping
    public ResponseEntity<DepartementDTO> createDepartement(
            @Valid @RequestBody DepartementCreateRequest request
    ) {
        DepartementDTO created = departementService.createDepartement(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{departementId}")
                .buildAndExpand(created.getDepartementId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{departementId}")
    public DepartementDTO updateDepartement(
            @PathVariable String departementId,
            @Valid @RequestBody DepartementUpdateRequest request
    ) {
        return departementService.updateDepartement(departementId, request);
    }

    @DeleteMapping("/{departementId}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable String departementId) {
        departementService.deleteDepartement(departementId);
        return ResponseEntity.noContent().build();
    }
}

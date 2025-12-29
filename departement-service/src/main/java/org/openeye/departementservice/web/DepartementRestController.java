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

    @GetMapping("/{departementCode}")
    public DepartementDTO getDepartementByCode(@PathVariable String departementCode) {
        return departementService.getDepartementByCode(departementCode);
    }

    @GetMapping("/active")
    public List<DepartementDTO> getActiveDepartements() {
        return departementService.getActiveDepartements();
    }

    @GetMapping("/active/count")
    public long countActiveDepartements() {
        return departementService.countActiveDepartements();
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
                .path("/{departementCode}")
                .buildAndExpand(created.getDepartementCode())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{departementCode}")
    public DepartementDTO updateDepartement(
            @PathVariable String departementCode,
            @Valid @RequestBody DepartementUpdateRequest request
    ) {
        return departementService.updateDepartement(departementCode, request);
    }

    @DeleteMapping("/{departementCode}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable String departementCode) {
        departementService.deleteDepartement(departementCode);
        return ResponseEntity.noContent().build();
    }
}

package com.labestages.controller;

import com.labestages.dto.EtudiantDTO;
import com.labestages.service.EtudiantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la ressource Étudiant.
 * Endpoints : GET/POST/PUT/DELETE /api/etudiants
 */
@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
@Tag(name = "Étudiants", description = "Gestion des étudiants de Licence 3")
public class EtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    @Operation(summary = "Lister tous les étudiants ou rechercher par matricule/nom")
    public ResponseEntity<List<EtudiantDTO>> getAll(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(etudiantService.search(search));
        }
        return ResponseEntity.ok(etudiantService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un étudiant par son identifiant")
    public ResponseEntity<EtudiantDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel étudiant")
    public ResponseEntity<EtudiantDTO> create(@Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity.status(201).body(etudiantService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un étudiant existant")
    public ResponseEntity<EtudiantDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity.ok(etudiantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un étudiant")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etudiantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

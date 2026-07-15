package com.labestages.controller;

import com.labestages.dto.EncadreurDTO;
import com.labestages.service.EncadreurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la ressource Encadreur.
 * Endpoints : GET/POST/PUT/DELETE /api/encadreurs
 */
@RestController
@RequestMapping("/api/encadreurs")
@RequiredArgsConstructor
@Tag(name = "Encadreurs", description = "Gestion des encadreurs et de leurs spécialités")
public class EncadreurController {

    private final EncadreurService encadreurService;

    @GetMapping
    @Operation(summary = "Lister tous les encadreurs")
    public ResponseEntity<List<EncadreurDTO>> getAll() {
        return ResponseEntity.ok(encadreurService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un encadreur par son identifiant")
    public ResponseEntity<EncadreurDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(encadreurService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel encadreur")
    public ResponseEntity<EncadreurDTO> create(@Valid @RequestBody EncadreurDTO dto) {
        return ResponseEntity.status(201).body(encadreurService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un encadreur existant")
    public ResponseEntity<EncadreurDTO> update(@PathVariable Long id,
                                                @Valid @RequestBody EncadreurDTO dto) {
        return ResponseEntity.ok(encadreurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un encadreur")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        encadreurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

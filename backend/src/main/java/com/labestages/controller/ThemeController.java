package com.labestages.controller;

import com.labestages.dto.ThemeDTO;
import com.labestages.dto.ThemeRequestDTO;
import com.labestages.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la ressource Thème/Projet.
 * Endpoints : GET/POST/PUT/DELETE /api/themes
 */
@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@Tag(name = "Thèmes / Projets", description = "Gestion des thèmes de projets de fin de cycle")
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    @Operation(summary = "Lister tous les thèmes")
    public ResponseEntity<List<ThemeDTO>> getAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un thème par son identifiant")
    public ResponseEntity<ThemeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(themeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Soumettre un nouveau thème et l'affecter à un étudiant et un encadreur")
    public ResponseEntity<ThemeDTO> create(@Valid @RequestBody ThemeRequestDTO request) {
        return ResponseEntity.status(201).body(themeService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un thème (y compris son statut)")
    public ResponseEntity<ThemeDTO> update(@PathVariable Long id,
                                            @Valid @RequestBody ThemeRequestDTO request) {
        return ResponseEntity.ok(themeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un thème")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

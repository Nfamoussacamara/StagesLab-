package com.labestages.service;

import com.labestages.dto.EtudiantDTO;
import com.labestages.entity.Etudiant;
import com.labestages.exception.BusinessException;
import com.labestages.exception.ResourceNotFoundException;
import com.labestages.repository.EtudiantRepository;
import com.labestages.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des étudiants.
 * Contient toute la logique : validation, unicité, mapping DTO.
 */
@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final ThemeRepository themeRepository;

    // ─── Lecture ─────────────────────────────────────────────

    public List<EtudiantDTO> findAll() {
        return etudiantRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EtudiantDTO findById(Long id) {
        return toDTO(getEntityOrThrow(id));
    }

    public List<EtudiantDTO> search(String terme) {
        return etudiantRepository.searchByMatriculeOrNom(terme)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── Création ────────────────────────────────────────────

    @Transactional
    public EtudiantDTO create(EtudiantDTO dto) {
        // Règle métier : matricule unique
        if (etudiantRepository.existsByMatricule(dto.getMatricule())) {
            throw new BusinessException("Un étudiant avec le matricule '" + dto.getMatricule() + "' existe déjà");
        }
        // Règle métier : email unique
        if (etudiantRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Un étudiant avec l'email '" + dto.getEmail() + "' existe déjà");
        }

        Etudiant etudiant = toEntity(dto);
        if (etudiant.getNiveau() == null || etudiant.getNiveau().isBlank()) {
            etudiant.setNiveau("L3");
        }

        return toDTO(etudiantRepository.save(etudiant));
    }

    // ─── Modification ─────────────────────────────────────────

    @Transactional
    public EtudiantDTO update(Long id, EtudiantDTO dto) {
        Etudiant existing = getEntityOrThrow(id);

        // Règle métier : matricule unique (en excluant l'ID actuel)
        if (etudiantRepository.existsByMatriculeAndIdNot(dto.getMatricule(), id)) {
            throw new BusinessException("Un étudiant avec le matricule '" + dto.getMatricule() + "' existe déjà");
        }
        // Règle métier : email unique (en excluant l'ID actuel)
        if (etudiantRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Un étudiant avec l'email '" + dto.getEmail() + "' existe déjà");
        }

        existing.setMatricule(dto.getMatricule());
        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setNiveau(dto.getNiveau() != null && !dto.getNiveau().isBlank() ? dto.getNiveau() : "L3");

        return toDTO(etudiantRepository.save(existing));
    }

    // ─── Suppression ──────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Étudiant", id);
        }
        // Supprimer d'abord le thème associé s'il existe pour éviter la violation de clé étrangère (integrity constraint)
        themeRepository.findByEtudiantId(id).ifPresent(themeRepository::delete);
        
        etudiantRepository.deleteById(id);
    }

    // ─── Helpers internes ─────────────────────────────────────

    private Etudiant getEntityOrThrow(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", id));
    }

    public EtudiantDTO toDTO(Etudiant e) {
        return new EtudiantDTO(e.getId(), e.getMatricule(), e.getNom(), e.getPrenom(), e.getEmail(), e.getNiveau());
    }

    private Etudiant toEntity(EtudiantDTO dto) {
        Etudiant e = new Etudiant();
        e.setMatricule(dto.getMatricule());
        e.setNom(dto.getNom());
        e.setPrenom(dto.getPrenom());
        e.setEmail(dto.getEmail());
        e.setNiveau(dto.getNiveau() != null ? dto.getNiveau() : "L3");
        return e;
    }
}

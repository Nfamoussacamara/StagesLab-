package com.labestages.service;

import com.labestages.dto.EncadreurDTO;
import com.labestages.entity.Encadreur;
import com.labestages.exception.BusinessException;
import com.labestages.exception.ResourceNotFoundException;
import com.labestages.repository.EncadreurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des encadreurs.
 */
@Service
@RequiredArgsConstructor
public class EncadreurService {

    private final EncadreurRepository encadreurRepository;

    // ─── Lecture ─────────────────────────────────────────────

    public List<EncadreurDTO> findAll() {
        return encadreurRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EncadreurDTO findById(Long id) {
        return toDTO(getEntityOrThrow(id));
    }

    // ─── Création ────────────────────────────────────────────

    @Transactional
    public EncadreurDTO create(EncadreurDTO dto) {
        if (encadreurRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Un encadreur avec l'email '" + dto.getEmail() + "' existe déjà");
        }
        return toDTO(encadreurRepository.save(toEntity(dto)));
    }

    // ─── Modification ─────────────────────────────────────────

    @Transactional
    public EncadreurDTO update(Long id, EncadreurDTO dto) {
        Encadreur existing = getEntityOrThrow(id);

        if (encadreurRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Un encadreur avec l'email '" + dto.getEmail() + "' existe déjà");
        }

        existing.setNom(dto.getNom());
        existing.setEmail(dto.getEmail());
        existing.setSpecialite(dto.getSpecialite());
        existing.setDisponibilite(dto.getDisponibilite());

        return toDTO(encadreurRepository.save(existing));
    }

    // ─── Suppression ──────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        if (!encadreurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Encadreur", id);
        }
        encadreurRepository.deleteById(id);
    }

    // ─── Helpers internes ─────────────────────────────────────

    private Encadreur getEntityOrThrow(Long id) {
        return encadreurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Encadreur", id));
    }

    public EncadreurDTO toDTO(Encadreur e) {
        return new EncadreurDTO(e.getId(), e.getNom(), e.getEmail(), e.getSpecialite(), e.getDisponibilite());
    }

    private Encadreur toEntity(EncadreurDTO dto) {
        Encadreur e = new Encadreur();
        e.setNom(dto.getNom());
        e.setEmail(dto.getEmail());
        e.setSpecialite(dto.getSpecialite());
        e.setDisponibilite(dto.getDisponibilite());
        return e;
    }
}

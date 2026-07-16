package com.labestages.service;

import com.labestages.dto.ThemeDTO;
import com.labestages.dto.ThemeRequestDTO;
import com.labestages.entity.Encadreur;
import com.labestages.entity.Etudiant;
import com.labestages.entity.StatutTheme;
import com.labestages.entity.Theme;
import com.labestages.exception.BusinessException;
import com.labestages.exception.ResourceNotFoundException;
import com.labestages.repository.EncadreurRepository;
import com.labestages.repository.EtudiantRepository;
import com.labestages.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des thèmes/projets de fin de cycle.
 */
@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final EtudiantRepository etudiantRepository;
    private final EncadreurRepository encadreurRepository;

    // ─── Lecture ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ThemeDTO> findAll() {
        return themeRepository.findAllWithEtudiantAndEncadreur()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ThemeDTO findById(Long id) {
        return toDTO(getEntityOrThrow(id));
    }

    // ─── Création ────────────────────────────────────────────

    @Transactional
    public ThemeDTO create(ThemeRequestDTO request) {
        // Règle métier : titre unique
        if (themeRepository.existsByTitre(request.getTitre())) {
            throw new BusinessException("Un thème avec le titre '" + request.getTitre() + "' existe déjà");
        }

        // Règle métier : un étudiant ne peut avoir qu'un seul thème
        if (themeRepository.existsByEtudiantId(request.getEtudiantId())) {
            throw new BusinessException("Cet étudiant est déjà associé à un thème");
        }

        Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", request.getEtudiantId()));

        Encadreur encadreur = encadreurRepository.findById(request.getEncadreurId())
                .orElseThrow(() -> new ResourceNotFoundException("Encadreur", request.getEncadreurId()));

        Theme theme = new Theme();
        theme.setTitre(request.getTitre());
        theme.setDescription(request.getDescription());
        theme.setDomaine(request.getDomaine());
        theme.setStatut(request.getStatut() != null ? request.getStatut() : StatutTheme.EN_ATTENTE);
        theme.setEtudiant(etudiant);
        theme.setEncadreur(encadreur);

        return toDTO(themeRepository.save(theme));
    }

    // ─── Modification ─────────────────────────────────────────

    @Transactional
    public ThemeDTO update(Long id, ThemeRequestDTO request) {
        Theme existing = getEntityOrThrow(id);

        // Vérifier l'unicité du titre en excluant l'ID actuel
        if (themeRepository.existsByTitreAndIdNot(request.getTitre(), id)) {
            throw new BusinessException("Un thème avec le titre '" + request.getTitre() + "' existe déjà");
        }

        // Si l'étudiant change, vérifier qu'il n'a pas déjà un autre thème
        if (!existing.getEtudiant().getId().equals(request.getEtudiantId())
                && themeRepository.existsByEtudiantId(request.getEtudiantId())) {
            throw new BusinessException("Cet étudiant est déjà associé à un thème");
        }

        Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", request.getEtudiantId()));

        Encadreur encadreur = encadreurRepository.findById(request.getEncadreurId())
                .orElseThrow(() -> new ResourceNotFoundException("Encadreur", request.getEncadreurId()));

        existing.setTitre(request.getTitre());
        existing.setDescription(request.getDescription());
        existing.setDomaine(request.getDomaine());
        existing.setStatut(request.getStatut() != null ? request.getStatut() : existing.getStatut());
        existing.setEtudiant(etudiant);
        existing.setEncadreur(encadreur);

        return toDTO(themeRepository.save(existing));
    }

    // ─── Suppression ──────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Thème", id);
        }
        themeRepository.deleteById(id);
    }

    // ─── Helpers internes ─────────────────────────────────────

    private Theme getEntityOrThrow(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thème", id));
    }

    public ThemeDTO toDTO(Theme t) {
        return new ThemeDTO(
                t.getId(),
                t.getTitre(),
                t.getDescription(),
                t.getDomaine(),
                t.getStatut(),
                t.getEtudiant().getId(),
                t.getEtudiant().getMatricule(),
                t.getEtudiant().getNom(),
                t.getEtudiant().getPrenom(),
                t.getEncadreur().getId(),
                t.getEncadreur().getNom(),
                t.getEncadreur().getSpecialite()
        );
    }
}

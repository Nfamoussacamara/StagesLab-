package com.labestages.service;

import com.labestages.dto.DashboardDTO;
import com.labestages.entity.StatutTheme;
import com.labestages.repository.EncadreurRepository;
import com.labestages.repository.EtudiantRepository;
import com.labestages.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour les statistiques du tableau de bord.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EtudiantRepository etudiantRepository;
    private final EncadreurRepository encadreurRepository;
    private final ThemeRepository themeRepository;

    public DashboardDTO getStats() {
        long totalEtudiants  = etudiantRepository.count();
        long totalEncadreurs = encadreurRepository.count();
        long totalThemes     = themeRepository.count();
        long enAttente       = themeRepository.countByStatut(StatutTheme.EN_ATTENTE);
        long valides         = themeRepository.countByStatut(StatutTheme.VALIDE);
        long soutenus        = themeRepository.countByStatut(StatutTheme.SOUTENU);
        long rejetes         = themeRepository.countByStatut(StatutTheme.REJETE);

        return new DashboardDTO(totalEtudiants, totalEncadreurs, totalThemes, enAttente, valides, soutenus, rejetes);
    }
}

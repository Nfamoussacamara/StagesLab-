package com.labestages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les statistiques du tableau de bord.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private long totalEtudiants;
    private long totalEncadreurs;
    private long totalThemes;
    private long themesEnAttente;
    private long themesValides;
    private long themesSoutenus;
    private long themesRejetes;
}

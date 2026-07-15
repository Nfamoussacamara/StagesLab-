package com.labestages.dto;

import com.labestages.entity.StatutTheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour un Thème.
 * Les informations de l'étudiant et de l'encadreur sont aplaties
 * (pas d'objets imbriqués) pour éviter toute boucle de sérialisation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDTO {
    private Long id;
    private String titre;
    private String description;
    private String domaine;
    private StatutTheme statut;

    // Informations aplaties de l'étudiant
    private Long etudiantId;
    private String etudiantMatricule;
    private String etudiantNom;
    private String etudiantPrenom;

    // Informations aplaties de l'encadreur
    private Long encadreurId;
    private String encadreurNom;
    private String encadreurSpecialite;
}

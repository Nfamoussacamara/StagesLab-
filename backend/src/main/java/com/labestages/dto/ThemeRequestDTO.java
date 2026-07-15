package com.labestages.dto;

import com.labestages.entity.StatutTheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requête pour la création / modification d'un Thème.
 * Reçoit les IDs de l'étudiant et de l'encadreur côté client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotBlank(message = "Le domaine est obligatoire")
    private String domaine;

    private StatutTheme statut = StatutTheme.EN_ATTENTE;

    @NotNull(message = "L'identifiant de l'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "L'identifiant de l'encadreur est obligatoire")
    private Long encadreurId;
}

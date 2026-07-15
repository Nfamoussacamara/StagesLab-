package com.labestages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour un Etudiant.
 * N'expose jamais l'entité JPA directement.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String niveau;
}

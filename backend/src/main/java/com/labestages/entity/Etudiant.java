package com.labestages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un étudiant de Licence 3.
 */
@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    @Column(unique = true, nullable = false)
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être au format valide")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String niveau = "L3";

    // Relation avec Thème : un étudiant a au plus un thème
    // Côté propriétaire : Theme.etudiant
    // On n'expose pas le thème ici pour éviter les boucles de sérialisation
}

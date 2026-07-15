package com.labestages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un encadreur de projets/stages.
 */
@Entity
@Table(name = "encadreurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encadreur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être au format valide")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "La spécialité est obligatoire")
    @Column(nullable = false)
    private String specialite;

    @NotNull(message = "La disponibilité est obligatoire")
    @Min(value = 0, message = "La disponibilité doit être un nombre positif ou nul")
    @Column(nullable = false)
    private Integer disponibilite;

    // Relation avec Thème : un encadreur peut avoir plusieurs thèmes
    // Côté propriétaire : Theme.encadreur
    // On n'expose pas les thèmes ici pour éviter les boucles de sérialisation
}

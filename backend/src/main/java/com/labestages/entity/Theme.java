package com.labestages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un thème / projet de fin de cycle.
 * Un thème appartient obligatoirement à un étudiant et à un encadreur.
 */
@Entity
@Table(name = "themes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Le domaine est obligatoire")
    @Column(nullable = false)
    private String domaine;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTheme statut = StatutTheme.EN_ATTENTE;

    // Relation ManyToOne vers Etudiant (un étudiant → un thème)
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    @NotNull(message = "L'étudiant est obligatoire")
    private Etudiant etudiant;

    // Relation ManyToOne vers Encadreur (un encadreur → plusieurs thèmes)
    @ManyToOne
    @JoinColumn(name = "encadreur_id", nullable = false)
    @NotNull(message = "L'encadreur est obligatoire")
    private Encadreur encadreur;
}

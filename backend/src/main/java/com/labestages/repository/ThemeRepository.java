package com.labestages.repository;

import com.labestages.entity.StatutTheme;
import com.labestages.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour l'entité Theme.
 */
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    // Vérifier l'unicité du titre
    boolean existsByTitre(String titre);

    // Vérifier l'unicité du titre en excluant l'ID actuel (pour la modification)
    boolean existsByTitreAndIdNot(String titre, Long id);

    // Compter les thèmes par statut (pour le dashboard)
    long countByStatut(StatutTheme statut);

    // Vérifier qu'un étudiant n'a pas déjà un thème affecté
    boolean existsByEtudiantId(Long etudiantId);

    // Trouver le thème d'un étudiant donné
    Optional<Theme> findByEtudiantId(Long etudiantId);

    // Lister les thèmes d'un encadreur donné
    List<Theme> findByEncadreurId(Long encadreurId);

    // Récupérer de façon Eager tous les thèmes avec leurs étudiants et encadreurs pour éviter N+1 requêtes et LazyInitializationException
    @org.springframework.data.jpa.repository.Query("SELECT t FROM Theme t JOIN FETCH t.etudiant JOIN FETCH t.encadreur")
    List<Theme> findAllWithEtudiantAndEncadreur();
}

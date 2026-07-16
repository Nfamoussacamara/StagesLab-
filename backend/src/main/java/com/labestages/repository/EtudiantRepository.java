package com.labestages.repository;

import com.labestages.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour l'entité Etudiant.
 */
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    // Vérifier l'unicité du matricule
    boolean existsByMatricule(String matricule);

    // Vérifier l'unicité du matricule en excluant l'ID actuel (pour la modification)
    boolean existsByMatriculeAndIdNot(String matricule, Long id);

    // Vérifier l'unicité de l'email
    boolean existsByEmail(String email);

    // Vérifier l'unicité de l'email en excluant l'ID actuel
    boolean existsByEmailAndIdNot(String email, Long id);

    // Recherche par matricule exact
    Optional<Etudiant> findByMatricule(String matricule);

    // Recherche par matricule OU nom OU prénom (insensible à la casse)
    @Query("SELECT e FROM Etudiant e WHERE " +
           "LOWER(e.matricule) LIKE LOWER(CONCAT('%', :terme, '%')) OR " +
           "LOWER(e.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR " +
           "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Etudiant> searchByMatriculeOrNom(@Param("terme") String terme);
}

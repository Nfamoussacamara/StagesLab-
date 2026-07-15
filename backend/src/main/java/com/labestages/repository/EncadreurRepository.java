package com.labestages.repository;

import com.labestages.entity.Encadreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité Encadreur.
 */
@Repository
public interface EncadreurRepository extends JpaRepository<Encadreur, Long> {

    // Vérifier si un encadreur avec cet email existe déjà
    boolean existsByEmail(String email);

    // Vérifier l'unicité de l'email en excluant l'ID actuel (pour la modification)
    boolean existsByEmailAndIdNot(String email, Long id);
}

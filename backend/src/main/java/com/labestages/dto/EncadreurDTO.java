package com.labestages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour un Encadreur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncadreurDTO {
    private Long id;
    private String nom;
    private String email;
    private String specialite;
    private Integer disponibilite;
}

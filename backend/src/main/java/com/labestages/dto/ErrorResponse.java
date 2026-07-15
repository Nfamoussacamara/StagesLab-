package com.labestages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Format JSON unifié pour toutes les réponses d'erreur de l'API.
 * Exemple : { "timestamp": "...", "status": 404, "message": "Étudiant introuvable" }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
}

package com.labestages.exception;

import com.labestages.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions — intercepte toutes les erreurs de l'API
 * et retourne une réponse JSON cohérente avec le bon code HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — Ressource introuvable
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 404, ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    // 400 — Violation de règle métier (matricule dupliqué, etc.)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 400, ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    // 400 — Validation Bean Validation (@NotBlank, @Email, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 400, "Données invalides : " + details);
        return ResponseEntity.status(400).body(error);
    }

    // 500 — Toute autre erreur non anticipée
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace(); // Log de la trace d'erreur pour le débogage console
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), 500, "Erreur interne du serveur : " + ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}

package com.labestages.exception;

/**
 * Exception métier lancée pour une violation de règle métier
 * (ex : matricule déjà utilisé, étudiant déjà affecté à un thème).
 * Interceptée par GlobalExceptionHandler → réponse HTTP 400.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}

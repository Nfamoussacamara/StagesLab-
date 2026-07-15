package com.labestages.exception;

/**
 * Exception métier lancée quand une ressource est introuvable en base.
 * Interceptée par GlobalExceptionHandler → réponse HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String ressource, Long id) {
        super(ressource + " avec l'identifiant " + id + " introuvable");
    }
}

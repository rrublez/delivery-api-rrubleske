package com.deliverytech.delivery.exception;

/**
 * Exceção lançada quando um recurso não é encontrado na base de dados.
 * Retorna HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

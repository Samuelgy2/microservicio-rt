package com.microserviviort.api.exception;

/**
 * Error de validacion de la peticion (HTTP 400).
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

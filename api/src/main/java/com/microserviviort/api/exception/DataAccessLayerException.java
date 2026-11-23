package com.microserviviort.api.exception;

/**
 * Error en la capa de acceso a datos (HTTP 500).
 */
public class DataAccessLayerException extends RuntimeException {
    public DataAccessLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}

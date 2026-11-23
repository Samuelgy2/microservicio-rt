package com.microserviviort.api.dto;

import java.time.LocalDateTime;

/**
 * Estructura estandar de respuesta de error.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(LocalDateTime.now(), status, error, message, path);
    }
}

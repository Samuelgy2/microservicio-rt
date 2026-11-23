package com.microserviviort.api.controller;

import com.microserviviort.api.dto.PageResponse;
import com.microserviviort.api.model.transaccion;
import com.microserviviort.api.service.TransaccionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API REST de transacciones: consultar (paginado) y eliminar.
 */
@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);

    private final TransaccionService service;

    public TransaccionController(TransaccionService service) {
        this.service = service;
    }

    /**
     * GET /api/transacciones?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<PageResponse<transaccion>> consultar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "${app.paginacion.tamano-defecto:10}") int size) {
        log.info("GET /api/transacciones page={} size={}", page, size);
        try {
            return ResponseEntity.ok(service.consultar(page, size));
        } catch (RuntimeException ex) {
            log.error("Fallo en GET /api/transacciones page={} size={}: {}", page, size, ex.getMessage());
            throw ex; // delegado a GlobalExceptionHandler (400/404/500)
        }
    }

    /**
     * GET /api/transacciones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<transaccion> consultarPorId(@PathVariable int id) {
        log.info("GET /api/transacciones/{}", id);
        try {
            return ResponseEntity.ok(service.consultarPorId(id));
        } catch (RuntimeException ex) {
            log.error("Fallo en GET /api/transacciones/{}: {}", id, ex.getMessage());
            throw ex;
        }
    }

    /**
     * DELETE /api/transacciones/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable int id) {
        log.info("DELETE /api/transacciones/{}", id);
        try {
            service.eliminar(id);
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", 200);
            body.put("message", "Transaccion " + id + " eliminada correctamente");
            body.put("id", id);
            return ResponseEntity.ok(body);
        } catch (RuntimeException ex) {
            log.error("Fallo en DELETE /api/transacciones/{}: {}", id, ex.getMessage());
            throw ex;
        }
    }
}

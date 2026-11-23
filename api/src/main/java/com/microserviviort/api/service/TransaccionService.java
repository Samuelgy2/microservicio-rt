package com.microserviviort.api.service;

import com.microserviviort.api.dto.PageResponse;
import com.microserviviort.api.exception.BadRequestException;
import com.microserviviort.api.exception.ResourceNotFoundException;
import com.microserviviort.api.model.transaccion;
import com.microserviviort.api.repository.TransaccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Logica de negocio para consultar y eliminar transacciones.
 * <p>
 * Sin {@code @Transactional}: cada operacion es una sola sentencia SQL (auto-commit),
 * y asi las validaciones se ejecutan antes de pedir una conexion a la BD.
 * Con {@code @Transactional} la conexion se abria al entrar al metodo, y un
 * parametro invalido respondia 500 en vez de 400 si la BD no estaba disponible.
 */
@Service
public class TransaccionService {

    private static final Logger log = LoggerFactory.getLogger(TransaccionService.class);

    private final TransaccionRepository repository;
    private final int tamanoMaximo;

    public TransaccionService(TransaccionRepository repository,
                              @Value("${app.paginacion.tamano-maximo:100}") int tamanoMaximo) {
        this.repository = repository;
        this.tamanoMaximo = tamanoMaximo;
    }

    public PageResponse<transaccion> consultar(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("El parametro 'page' debe ser mayor o igual a 0");
        }
        if (size < 1 || size > tamanoMaximo) {
            throw new BadRequestException("El parametro 'size' debe estar entre 1 y " + tamanoMaximo);
        }
        long total = repository.count();
        List<transaccion> contenido = repository.findAll(page, size);
        log.info("Consulta de transacciones: page={}, size={}, devueltas={}, total={}",
                page, size, contenido.size(), total);
        return PageResponse.of(contenido, page, size, total);
    }

    public transaccion consultarPorId(int id) {
        validarId(id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Transaccion id={} no encontrada", id);
                    return new ResourceNotFoundException("No existe la transaccion con id " + id);
                });
    }

    public void eliminar(int id) {
        validarId(id);
        int filas = repository.deleteById(id);
        if (filas == 0) {
            log.warn("Intento de eliminar transaccion inexistente id={}", id);
            throw new ResourceNotFoundException("No existe la transaccion con id " + id);
        }
        log.info("Transaccion id={} eliminada correctamente", id);
    }

    private void validarId(int id) {
        if (id <= 0) {
            throw new BadRequestException("El id debe ser un entero positivo");
        }
    }
}

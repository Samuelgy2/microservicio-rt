package com.microserviviort.api.repository;

import com.microserviviort.api.exception.DataAccessLayerException;
import com.microserviviort.api.model.transaccion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de transacciones mediante JDBC (JdbcTemplate) sobre MySQL.
 */
@Repository
public class TransaccionRepository {

    private static final Logger log = LoggerFactory.getLogger(TransaccionRepository.class);

    private static final String COLUMNAS =
            "id, tipo, monto, fecha, descripcion, created_at, metodo_pago, categoria_id, " +
            "referencia, created_by, id_socio, estado";

    private final JdbcTemplate jdbcTemplate;
    private final String tabla;

    private static final RowMapper<transaccion> ROW_MAPPER = (rs, rowNum) -> {
        transaccion t = new transaccion();
        t.setId(rs.getObject("id", Integer.class));
        t.setTipo(rs.getString("tipo"));
        t.setMonto(rs.getBigDecimal("monto"));
        Date fecha = rs.getDate("fecha");
        t.setFecha(fecha != null ? fecha.toLocalDate() : null);
        t.setDescripcion(rs.getString("descripcion"));
        Timestamp created = rs.getTimestamp("created_at");
        t.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        t.setMetodoPago(rs.getString("metodo_pago"));
        t.setCategoriaId(rs.getObject("categoria_id", Integer.class));
        t.setReferencia(rs.getString("referencia"));
        t.setCreatedBy(rs.getObject("created_by", Integer.class));
        t.setIdSocio(rs.getObject("id_socio", Integer.class));
        t.setEstado(rs.getString("estado"));
        return t;
    };

    public TransaccionRepository(JdbcTemplate jdbcTemplate,
                                 @Value("${app.tabla.transacciones:transacciones}") String tabla) {
        // El nombre de tabla proviene de configuracion; se valida para evitar inyeccion SQL.
        if (!tabla.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("Nombre de tabla invalido: " + tabla);
        }
        this.jdbcTemplate = jdbcTemplate;
        this.tabla = tabla;
        log.info("TransaccionRepository inicializado sobre la tabla '{}'", tabla);
    }

    /**
     * Consulta paginada ordenada por fecha e id descendente.
     */
    public List<transaccion> findAll(int page, int size) {
        String sql = "SELECT " + COLUMNAS + " FROM " + tabla +
                " ORDER BY fecha DESC, id DESC LIMIT ? OFFSET ?";
        long offset = (long) page * size;
        try {
            log.debug("Ejecutando consulta paginada: page={}, size={}, offset={}", page, size, offset);
            return jdbcTemplate.query(sql, ROW_MAPPER, size, offset);
        } catch (DataAccessException ex) {
            log.error("Error consultando transacciones (page={}, size={}): {}", page, size, ex.getMessage(), ex);
            throw new DataAccessLayerException("Error al consultar las transacciones", ex);
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM " + tabla;
        try {
            Long total = jdbcTemplate.queryForObject(sql, Long.class);
            return total != null ? total : 0L;
        } catch (DataAccessException ex) {
            log.error("Error contando transacciones: {}", ex.getMessage(), ex);
            throw new DataAccessLayerException("Error al contar las transacciones", ex);
        }
    }

    public Optional<transaccion> findById(int id) {
        String sql = "SELECT " + COLUMNAS + " FROM " + tabla + " WHERE id = ?";
        try {
            List<transaccion> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
            return result.stream().findFirst();
        } catch (DataAccessException ex) {
            log.error("Error consultando transaccion id={}: {}", id, ex.getMessage(), ex);
            throw new DataAccessLayerException("Error al consultar la transaccion " + id, ex);
        }
    }

    /**
     * @return numero de filas eliminadas (0 si no existia).
     */
    public int deleteById(int id) {
        String sql = "DELETE FROM " + tabla + " WHERE id = ?";
        try {
            int filas = jdbcTemplate.update(sql, id);
            log.debug("DELETE id={} -> filas afectadas={}", id, filas);
            return filas;
        } catch (DataAccessException ex) {
            log.error("Error eliminando transaccion id={}: {}", id, ex.getMessage(), ex);
            throw new DataAccessLayerException("Error al eliminar la transaccion " + id, ex);
        }
    }
}

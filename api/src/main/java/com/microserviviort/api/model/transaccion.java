package com.microserviviort.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class transaccion {

    private Integer id;                 // id INT AUTO_INCREMENT
    private String tipo;                // 'ingreso' | 'gasto'
    private BigDecimal monto;           // DECIMAL(15,2)
    private LocalDate fecha;            // DATE NOT NULL
    private String descripcion;         // TEXT
    private LocalDateTime createdAt;    // created_at TIMESTAMP
    private String metodoPago;          // metodo_pago: 'efectivo' | 'transferencia' | 'tarjeta' | 'cheque' | null
    private Integer categoriaId;        // categoria_id
    private String referencia;          // referencia (única si no es null)
    private Integer createdBy;          // created_by
    private Integer idSocio;            // id_socio
    private String estado = "confirmada"; // 'pendiente' | 'confirmada' | 'anulada'

    public transaccion() {
    }

    public transaccion(Integer id, String tipo, BigDecimal monto, LocalDate fecha, String descripcion,
                       LocalDateTime createdAt, String metodoPago, Integer categoriaId, String referencia,
                       Integer createdBy, Integer idSocio, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.createdAt = createdAt;
        this.metodoPago = metodoPago;
        this.categoriaId = categoriaId;
        this.referencia = referencia;
        this.createdBy = createdBy;
        this.idSocio = idSocio;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Integer getIdSocio() { return idSocio; }
    public void setIdSocio(Integer idSocio) { this.idSocio = idSocio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
// src/main/java/com/bdcopre/dto/LoanDTO.java
package com.kredio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanDTO {

    private Long id;
    private BigDecimal monto;
    private String estado;          // Puedes cambiar a LoanStatus si usas enum en el futuro
    private LocalDate fechaDesembolso;
    private Integer plazo;
    private BigDecimal tasaInteres;
    private String nombreCliente;

    // Campos adicionales necesarios para creación y visualización completa
    private String tipoPlan;        // "automatico" o "manual"
    private String frecuencia;      // "MENSUAL", "QUINCENAL", etc.
    private Long clienteId;         // ID del cliente/borrower (para creación y referencia)

    // Constructor vacío (útil para frameworks como Jackson)
    public LoanDTO() {}

    // Constructor completo con los 10 parámetros (coincide con tu llamada en convertToDTO)
    public LoanDTO(Long id, BigDecimal monto, String estado, LocalDate fechaDesembolso,
                   Integer plazo, BigDecimal tasaInteres, String nombreCliente,
                   String tipoPlan, String frecuencia, Long clienteId) {
        this.id = id;
        this.monto = monto;
        this.estado = estado;
        this.fechaDesembolso = fechaDesembolso;
        this.plazo = plazo;
        this.tasaInteres = tasaInteres;
        this.nombreCliente = nombreCliente;
        this.tipoPlan = tipoPlan;
        this.frecuencia = frecuencia;
        this.clienteId = clienteId;
    }

    // Getters y Setters para TODOS los campos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaDesembolso() { return fechaDesembolso; }
    public void setFechaDesembolso(LocalDate fechaDesembolso) { this.fechaDesembolso = fechaDesembolso; }

    public Integer getPlazo() { return plazo; }
    public void setPlazo(Integer plazo) { this.plazo = plazo; }

    public BigDecimal getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(BigDecimal tasaInteres) { this.tasaInteres = tasaInteres; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTipoPlan() { return tipoPlan; }
    public void setTipoPlan(String tipoPlan) { this.tipoPlan = tipoPlan; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
package com.kredio.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamo_id", nullable = false)
    private Loan prestamo;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;                // 1, 2, 3...

    @Column(name = "monto_esperado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoEsperado;           // Cuota fija calculada

    @Column(name = "monto_pagado", precision = 12, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal moraAplicada = BigDecimal.ZERO;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(nullable = false)
    private String estado = "PENDIENTE";        // PENDIENTE, PAGADO, PARCIAL, MORA

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "recibo_url", length = 500)
    private String reciboUrl;

    @Column(name = "registrado_por")
    private Long registradoPor;

    @Column(nullable = false)
    private boolean offline = false;

    @Column(nullable = false)
    private boolean sincronizado = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
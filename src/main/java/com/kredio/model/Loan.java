package com.kredio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestamos")
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal monto;

    @NotNull
    @Positive
    @Column(name = "tasa_interes")
    private BigDecimal tasaInteres;

    @NotNull
    @Min(1)
    private Integer plazo;

    @NotBlank
    @Column(nullable = false)
    private String frecuencia;

    @NotNull
    @Column(name = "fecha_desembolso")
    private LocalDate fechaDesembolso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus estado = LoanStatus.ACTIVO;

    @NotBlank
    @Column(name = "tipo_plan")
    private String tipoPlan = "automatico";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Client cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartera_id")  // ← Asegúrate que coincida con el nombre en BD
    private Portfolio cartera;  // ← CAMPO AGREGADO: relación inversa


    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> pagos = new ArrayList<>();

    // Auditoría
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public BigDecimal calcularCuota() {
        return BigDecimal.ZERO; // placeholder
    }
}
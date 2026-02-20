package com.kredio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "email_contacto", nullable = false, unique = true)
    private String emailContacto;

    private String telefono;
    private String direccion;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    private String planSuscripcion = "trial";

    private LocalDate fechaInicioSuscripcion;

    @Column(nullable = false)
    private boolean activo = true;

    // Auditoría básica (agregar para RF-10)
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
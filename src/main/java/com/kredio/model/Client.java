package com.kredio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String dui;

    private String telefono;
    private String direccion;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;  // ← Cambio clave: relación real en vez de solo Long

    // Elimina el tenantId manual si usas la relación arriba
    // private Long tenantId;  // ← borra este campo

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> prestamos = new ArrayList<>();  // Para facilitar consultas

    // Auditoría
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Dentro de la clase Client

    /**
     * Devuelve el ID del tenant asociado (wrapper para compatibilidad)
     */
    public Long getTenantId() {
        return tenant != null ? tenant.getId() : null;
    }

    /**
     * Asigna un tenant por su ID (wrapper para compatibilidad)
     */
    public void setTenantId(Long tenantId) {
        if (tenantId != null) {
            // Crea un objeto Tenant "referencia" (solo con ID)
            Tenant temp = new Tenant();
            temp.setId(tenantId);
            this.tenant = temp;
        } else {
            this.tenant = null;
        }
    }
}
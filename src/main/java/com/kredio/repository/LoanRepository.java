// src/main/java/com/bdcopre/repository/LoanRepository.java
package com.kredio.repository;

import com.kredio.model.Loan;
import com.kredio.model.LoanStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // 1. Filtrado por tenant (multi-tenant - muy importante)
    List<Loan> findByClienteTenantId(Long tenantId);

    // 2. Préstamos por cliente
    List<Loan> findByClienteId(Long clienteId);

    // 3. Por estado (activo, mora, pagado, refinanciado, etc.)
    List<Loan> findByEstado(LoanStatus estado);

    // 4. Préstamos en mora (vencidos y no pagados completamente)
    @Query("SELECT l FROM Loan l " +
            "WHERE l.estado IN ('ACTIVO', 'PARCIAL') " +
            "AND EXISTS (SELECT p FROM Payment p WHERE p.prestamo = l AND p.fechaVencimiento < :hoy AND p.estado IN ('PENDIENTE', 'PARCIAL'))")
    List<Loan> findLoansInMora(@Param("hoy") LocalDate hoy);

    // 5. Conteo por estado (para dashboard)
    long countByEstado(LoanStatus estado);

    @Query("SELECT COUNT(l) FROM Loan l " +
            "WHERE l.estado = :estado " +
            "AND l.cliente.tenant.id = :tenantId")
    long countByEstadoAndTenantId(
            @Param("estado") LoanStatus estado,
            @Param("tenantId") Long tenantId
    );

    // 6. Suma de montos por estado (capital desembolsado, pendiente, mora)
    @Query("SELECT COALESCE(SUM(l.monto), 0) FROM Loan l WHERE l.estado = :estado")
    BigDecimal sumMontoByEstado(@Param("estado") LoanStatus estado);

    @Query("SELECT COALESCE(SUM(l.monto), 0) FROM Loan l WHERE l.estado = :estado AND l.cliente.tenant.id = :tenantId")
    BigDecimal sumMontoByEstadoAndTenant(@Param("estado") LoanStatus estado, @Param("tenantId") Long tenantId);

    // 7. Préstamos activos o en mora de un cobrador (si tienes cartera/portafolio)
    // Asumiendo que Loan tiene relación con Portfolio/Cartera y Portfolio con Collector (User)
    // @Query("SELECT l FROM Loan l " +
            // "JOIN l.cliente c " +
            // "JOIN c.cartera p " +  // ajusta según tu modelo real
            // "WHERE p.collector.id = :collectorId " +
            // "AND l.estado IN ('ACTIVO', 'PARCIAL')")
    // List<Loan> findActiveLoansByCollector(@Param("collectorId") Long collectorId);

    // 8. Cargar préstamo con cliente y pagos (evitar N+1)
    @EntityGraph(attributePaths = {"cliente", "pagos"})
    Optional<Loan> findById(Long id);

    // 9. Préstamos próximos a vencer (para notificaciones)
    @Query("SELECT l FROM Loan l " +
            "WHERE EXISTS (SELECT p FROM Payment p WHERE p.prestamo = l " +
            "AND p.fechaVencimiento BETWEEN :inicio AND :fin " +
            "AND p.estado = 'PENDIENTE')")
    List<Loan> findLoansNearDueDate(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    // 10. Préstamos refinanciados (si tienes estado REFINANCIADO)
    List<Loan> findByEstadoOrderByFechaDesembolsoDesc(LoanStatus estado);
}
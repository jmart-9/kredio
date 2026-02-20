package com.kredio.service;

import com.kredio.dto.LoanDTO;
import com.kredio.model.Client;
import com.kredio.model.Loan;
import com.kredio.model.LoanStatus;
import com.kredio.model.Payment;
import com.kredio.repository.ClientRepository;
import com.kredio.repository.LoanRepository;
import com.kredio.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;  // ← ESTA LÍNEA FALTABA

    public LoanService(
            LoanRepository loanRepository,
            ClientRepository clientRepository,
            PaymentRepository paymentRepository
    ) {
        this.loanRepository = loanRepository;
        this.clientRepository = clientRepository;
        this.paymentRepository = paymentRepository;
    }

    private LoanDTO convertToDTO(Loan loan) {
        String nombreCliente = (loan.getCliente() != null) ? loan.getCliente().getNombre() : "Desconocido";
        return new LoanDTO(
                loan.getId(),
                loan.getMonto(),
                loan.getEstado().name(),
                loan.getFechaDesembolso(),
                loan.getPlazo(),
                loan.getTasaInteres(),
                nombreCliente,
                loan.getTipoPlan(),
                loan.getFrecuencia(),
                loan.getCliente() != null ? loan.getCliente().getId() : null
        );
    }

    private Loan convertToEntity(LoanDTO dto) {
        Loan loan = new Loan();
        loan.setMonto(dto.getMonto());
        loan.setTasaInteres(dto.getTasaInteres());
        loan.setPlazo(dto.getPlazo());
        loan.setFrecuencia(dto.getFrecuencia());
        loan.setFechaDesembolso(dto.getFechaDesembolso());
        loan.setTipoPlan(dto.getTipoPlan() != null ? dto.getTipoPlan() : "automatico");
        loan.setEstado(LoanStatus.ACTIVO);

        if (dto.getClienteId() == null) {
            throw new IllegalArgumentException("Cliente ID requerido");
        }
        Client cliente = clientRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        loan.setCliente(cliente);

        return loan;
    }

    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanDTO createLoan(LoanDTO loanDTO) {
        Loan loan = convertToEntity(loanDTO);
        Loan saved = loanRepository.save(loan);

        if ("automatico".equals(saved.getTipoPlan())) {
            generarCuotasAutomaticas(saved);
        }

        return convertToDTO(saved);
    }

    private void generarCuotasAutomaticas(Loan loan) {
        BigDecimal monto = loan.getMonto();
        BigDecimal tasaMensual = loan.getTasaInteres()
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        int plazo = loan.getPlazo();
        BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaMensual);
        BigDecimal cuota = monto.multiply(tasaMensual.multiply(unoMasTasa.pow(plazo)))
                .divide(unoMasTasa.pow(plazo).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        BigDecimal saldo = monto;
        LocalDate fechaCuota = loan.getFechaDesembolso().plusMonths(1); // Primera cuota al mes siguiente

        List<Payment> cuotas = new ArrayList<>();

        for (int i = 1; i <= plazo; i++) {
            BigDecimal interes = saldo.multiply(tasaMensual);
            BigDecimal capital = cuota.subtract(interes);
            saldo = saldo.subtract(capital).max(BigDecimal.ZERO); // Evitar negativos por redondeo

            Payment cuotaProgramada = new Payment();
            cuotaProgramada.setPrestamo(loan);
            cuotaProgramada.setNumeroCuota(i);
            cuotaProgramada.setMontoEsperado(cuota);
            cuotaProgramada.setMontoPagado(BigDecimal.ZERO);
            cuotaProgramada.setMoraAplicada(BigDecimal.ZERO);
            cuotaProgramada.setFechaVencimiento(fechaCuota);
            cuotaProgramada.setEstado("PENDIENTE");
            cuotaProgramada.setOffline(false);
            cuotaProgramada.setSincronizado(true);

            cuotas.add(cuotaProgramada);

            fechaCuota = fechaCuota.plusMonths(1);
        }

        // Guardar todas las cuotas en BD
        paymentRepository.saveAll(cuotas);

        // Opcional: asociarlas al préstamo (si tienes la lista en Loan)
        // loan.setPagos(cuotas);
        // loanRepository.save(loan);

        System.out.println("Generadas " + plazo + " cuotas automáticas de " + cuota + " cada una.");
    }
}
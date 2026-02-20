// src/main/java/com/bdcopre/controller/LoanController.java
package com.kredio.controller;

import com.kredio.dto.LoanDTO;
import com.kredio.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:53248")  // OK para dev; en prod usa config global o per-env
public class LoanController {

    @Autowired
    private LoanService loanService;

    // GET todos los préstamos (deberías filtrar por tenant en el service!)
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // POST crear préstamo (versión básica para MVP)
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
        LoanDTO createdLoan = loanService.createLoan(loanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
    }

    // Opcional: si quieres manejar errores explícitos o devolver solo ID
    // public ResponseEntity<?> createLoan(@Valid @RequestBody LoanDTO loanDTO) {
    //     try {
    //         LoanDTO created = loanService.createLoan(loanDTO);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(created);
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body("Error al crear préstamo: " + e.getMessage());
    //     }
    // }
}
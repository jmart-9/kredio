package com.kredio.controller;

import com.kredio.exception.TenantAlreadyExistsException;
import com.kredio.model.Tenant;
import com.kredio.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*") // Solo para desarrollo
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping
    public ResponseEntity<?> createTenant(@RequestBody Tenant tenant) {
        try {
            Tenant saved = tenantService.createTenant(tenant);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (TenantAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
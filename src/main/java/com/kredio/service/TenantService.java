package com.kredio.service;

import com.kredio.exception.TenantAlreadyExistsException;
import com.kredio.model.Tenant;
import com.kredio.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public Tenant createTenant(Tenant tenant) {
        if (tenantRepository.existsByEmailContacto(tenant.getEmailContacto())) {
            throw new TenantAlreadyExistsException("Ya existe una institución con ese email: " + tenant.getEmailContacto());
        }
        tenant.setActivo(true);
        if (tenant.getPlanSuscripcion() == null || tenant.getPlanSuscripcion().isEmpty()) {
            tenant.setPlanSuscripcion("trial");
        }
        return tenantRepository.save(tenant);
    }
}
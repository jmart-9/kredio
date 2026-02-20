package com.kredio.repository;

import com.kredio.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    boolean existsByEmailContacto(String emailContacto);
}
// src/main/java/com/bdcopre/repository/UserRepository.java
package com.kredio.repository;

import com.kredio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Optional: filter by tenant (for multi-tenant security)
    // List<User> findByTenantId(Long tenantId);
}
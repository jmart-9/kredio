// src/main/java/com/bdcopre/repository/ClientRepository.java
package com.kredio.repository;

import com.kredio.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
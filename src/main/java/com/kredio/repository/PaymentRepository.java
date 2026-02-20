// src/main/java/com/bdcopre/repository/PaymentRepository.java
package com.kredio.repository;

import com.kredio.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
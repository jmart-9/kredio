// src/main/java/com/bdcopre/model/Role.java
package com.kredio.model;

public enum Role {
    GLOBAL_ADMIN,      // Global Admin: manages tenants, plans, billing
    LENDER,            // Lender (Prestamista/Institución): owns the tenant
    CREDIT_ANALYST,    // Credit Analyst: approves loans, manages borrowers
    COLLECTOR,         // Collector: registers payments in field (mobile app)
    CASHIER            // Cashier: branch payments
}
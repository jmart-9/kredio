// src/main/java/com/bdcopre/model/LoanStatus.java
package com.kredio.model;

public enum LoanStatus {
    ACTIVO,
    PAGADO,
    MORA,
    REFINANCIADO,
    RECHAZADO,     // útil para préstamos no aprobados
    CANCELADO      // opcional, según tu flujo de negocio
}
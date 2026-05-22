package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.time.LocalDateTime;

public record ConsultarPresupuestoResponse(
        String categoria,
        double limite,
        double montoGastado,
        double restante,
        double porcentajeUsado,
        String periodo,
        Boolean activo,
        LocalDateTime fechaExpiracion,
        String alerta) {
}

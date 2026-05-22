package com.fabrica.gestionfinancierapersonal.application.dtos;

public record PresupuestoListadoResponse(
        String categoria,
        double limite,
        String periodo,
        double montoGastado,
        double porcentajeUsado,
        double restante,
        String alerta) {
}

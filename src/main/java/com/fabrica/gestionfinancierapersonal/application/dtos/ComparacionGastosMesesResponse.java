package com.fabrica.gestionfinancierapersonal.application.dtos;

public record ComparacionGastosMesesResponse(
        double gastoMesActual,
        double gastoMesAnterior,
        double diferencia,
        String mensaje) {
}

package com.fabrica.gestionfinancierapersonal.application.dtos;

public record ResumenFinancieroResponse(
        double totalIngresos,
        double totalGastos,
        double balanceNeto) {
}
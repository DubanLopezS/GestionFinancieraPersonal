package com.fabrica.gestionfinancierapersonal.application.dtos;

public record ResumenCategoriasResponse(
        String nombreCategoria,
        double total,
        double porcentaje) {
}

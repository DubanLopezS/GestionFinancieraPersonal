package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record CrearPresupuestoRequest(
        UUID usuarioId,
        UUID categoriaId,
        double limite,
        String periodo) {
}

package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record EditarPresupuestoRequest(
        UUID idUsuario,
        UUID idCategoria,
        double nuevoLimite,
        String nuevoPeriodo) {
}

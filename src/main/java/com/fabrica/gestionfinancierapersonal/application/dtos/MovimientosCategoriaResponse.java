package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record MovimientosCategoriaResponse(
        UUID idTransaccion,
        double monto,
        String tipo,
        String categoria,
        LocalDateTime fecha) {
}

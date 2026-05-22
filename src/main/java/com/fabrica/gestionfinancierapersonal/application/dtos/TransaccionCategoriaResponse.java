package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransaccionCategoriaResponse(
        UUID idTransaccion,
        double monto,
        LocalDateTime fecha,
        String nombreCuenta

) {
}

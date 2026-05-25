package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record TransferenciaResponse(
        UUID transferenciaId,
        UUID cuentaOrigen,
        UUID cuentaDestino,
        double trm) {
}
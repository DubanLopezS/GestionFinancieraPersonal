package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record TransferenciaRequest(
    UUID idCuentaOrigen,
    UUID idCuentaDestino,
    UUID idUsuario,
    double montoOrigen,
    double montoDestino
) {}
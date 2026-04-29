package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record CuentaResponse(
    UUID idCuenta,
    String nombre,
    double saldo,
    String moneda
) {}
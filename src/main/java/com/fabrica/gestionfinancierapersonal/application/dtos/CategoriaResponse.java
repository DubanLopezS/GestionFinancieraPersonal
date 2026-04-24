package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record CategoriaResponse(
    UUID id,
    String nombre,
    String tipo
) {}
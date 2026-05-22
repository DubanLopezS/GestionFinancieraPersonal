package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record MayorGastoCategoriaResponse(
                UUID idCategoria,
                String nombreCategoria,
                double montoTotal) {
}

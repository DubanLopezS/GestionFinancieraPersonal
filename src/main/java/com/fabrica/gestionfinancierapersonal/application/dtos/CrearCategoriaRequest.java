package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;


public record CrearCategoriaRequest(
    UUID idUsuario,
    String nombre,
    String tipo,
    String icono,
    String color) {
}

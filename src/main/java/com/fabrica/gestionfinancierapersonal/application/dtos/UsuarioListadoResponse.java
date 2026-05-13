package com.fabrica.gestionfinancierapersonal.application.dtos;

public record UsuarioListadoResponse(
        String nombre,
        String correo,
        String telefono,
        String username) {
}

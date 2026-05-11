package com.fabrica.gestionfinancierapersonal.application.dtos;

public record RestablecerPasswordRequest(
        String codigo,
        String nuevaPassword,
        String confirmacionPassword) {
}

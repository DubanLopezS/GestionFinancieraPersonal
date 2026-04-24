package com.fabrica.gestionfinancierapersonal.application.dtos;

import java.util.UUID;

public record ActualizarTransaccionRequest(
    UUID idTransaccion,
    UUID idUsuario,
    UUID idCategoria // Puede que esa transacción ya tenga una categoria
) {}

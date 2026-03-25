package com.fabrica.gestionfinancierapersonal.application.dtos;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class RegistrarUsuarioResponse {

    private String id;
    private String nombre;
    private String correo;
}
package com.fabrica.gestionfinancierapersonal.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegistrarUsuarioRequest {

    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;

}
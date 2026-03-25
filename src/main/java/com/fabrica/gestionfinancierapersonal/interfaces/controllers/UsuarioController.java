package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.RegistrarUsuario;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final RegistrarUsuario registrarUsuario;

    public UsuarioController(RegistrarUsuario registrarUsuario) {
        this.registrarUsuario = registrarUsuario;
    }

    // REGISTRAR USUARIO
    @PostMapping
    public RegistrarUsuarioResponse registrar(@RequestBody RegistrarUsuarioRequest request) {
        try {
            return registrarUsuario.ejecutar(request);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // LOGIN (para cuando lo implementes)
    /*
     * @PostMapping("/login")
     * public LoginResponse login(@RequestBody LoginRequest request) {
     * return loginUsuario.ejecutar(request);
     * }
     */
}

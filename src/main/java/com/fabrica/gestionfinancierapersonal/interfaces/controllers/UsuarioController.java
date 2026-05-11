package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import com.fabrica.gestionfinancierapersonal.application.usecases.LoginUsuario;
import com.fabrica.gestionfinancierapersonal.application.usecases.LogoutUsuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.LoginUsuarioRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.LoginUsuarioResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.RestablecerPasswordRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.SolicitarRecuperacionRequest;
import com.fabrica.gestionfinancierapersonal.application.usecases.RegistrarUsuario;
import com.fabrica.gestionfinancierapersonal.application.usecases.RestablecerPassword;
import com.fabrica.gestionfinancierapersonal.application.usecases.SolicitarRecuperacionPassword;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final LoginUsuario loginUsuario;
    private final RegistrarUsuario registrarUsuario;
    private final LogoutUsuario logoutUsuario;
    private final SolicitarRecuperacionPassword solicitarRecuperacionPassword;
    private final RestablecerPassword restablecerPassword;

    public UsuarioController(RegistrarUsuario registrarUsuario, LoginUsuario loginUsuario,
            LogoutUsuario logoutUsuario, SolicitarRecuperacionPassword solicitarRecuperacionPassword,
            RestablecerPassword restablecerPassword) {
        this.registrarUsuario = registrarUsuario;
        this.loginUsuario = loginUsuario;
        this.logoutUsuario = logoutUsuario;
        this.solicitarRecuperacionPassword = solicitarRecuperacionPassword;
        this.restablecerPassword = restablecerPassword;
    }

    // Registrar usuario
    @PostMapping("/signup")
    public RegistrarUsuarioResponse registrar(@RequestBody RegistrarUsuarioRequest request) {
        try {
            return registrarUsuario.ejecutar(request);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Login usuario
    @PostMapping("/login")
    public LoginUsuarioResponse login(@RequestBody LoginUsuarioRequest request) {
        try {
            return loginUsuario.ejecutar(request);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Logout usuario
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logoutUsuario.ejecutar();
        return ResponseEntity.noContent().build();
    }

    // Solicitud de recuperación de contraseña
    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(
            @RequestBody SolicitarRecuperacionRequest request) {
        try {
            solicitarRecuperacionPassword.ejecutar(request);
            return ResponseEntity.ok("Código de recuperación enviado, revise su bandeja de entrada");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Restablecer contraseña
    @PostMapping("/restablecer-password")
    public ResponseEntity<String> restablecerPassword(
            @RequestBody RestablecerPasswordRequest request) {
        try {
            restablecerPassword.ejecutar(request);
            return ResponseEntity.ok(
                    "Contraseña actualizada correctamente");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }
}

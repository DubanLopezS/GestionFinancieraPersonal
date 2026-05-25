package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import com.fabrica.gestionfinancierapersonal.application.usecases.LoginUsuario;
import com.fabrica.gestionfinancierapersonal.application.usecases.LogoutUsuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrarUsuarioResponse registrar(@RequestBody RegistrarUsuarioRequest request) {
        return registrarUsuario.ejecutar(request);
    }

    // Login usuario
    @PostMapping("/login")
    public ResponseEntity<LoginUsuarioResponse> login(@RequestBody LoginUsuarioRequest request) {
        return ResponseEntity.ok(
                loginUsuario.ejecutar(request));
    }

    // Logout usuario
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logoutUsuario.ejecutar();
        return ResponseEntity.noContent().build();
    }

    // Solicitud de recuperación de contraseña
    @PostMapping("/recuperarPassword")
    public ResponseEntity<String> recuperarPassword(@RequestBody SolicitarRecuperacionRequest request) {
        solicitarRecuperacionPassword.ejecutar(request);
        return ResponseEntity.ok("Código de recuperación enviado, revise su bandeja de entrada");
    }

    // Restablecer contraseña
    @PostMapping("/restablecerPassword")
    public ResponseEntity<String> restablecerPassword(@RequestBody RestablecerPasswordRequest request) {
        restablecerPassword.ejecutar(request);
        return ResponseEntity.ok("Contraseña actualizada correctamente");   
    }
}

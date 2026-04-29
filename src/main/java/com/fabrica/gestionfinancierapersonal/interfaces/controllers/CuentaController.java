package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCuentaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCuentaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.CuentaResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.CrearCuenta;
import com.fabrica.gestionfinancierapersonal.application.usecases.ListarCuentasPorUsuario;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CrearCuenta crearCuentaUseCase;
    private final ListarCuentasPorUsuario listarCuentasPorUsuarioUseCase;

    public CuentaController(CrearCuenta crearCuentaUseCase, ListarCuentasPorUsuario listarCuentasPorUsuarioUseCase) {
        this.crearCuentaUseCase = crearCuentaUseCase;
        this.listarCuentasPorUsuarioUseCase = listarCuentasPorUsuarioUseCase;
    }

    // Crear cuenta
    @PostMapping("/crear")
    public ResponseEntity<CrearCuentaResponse> crearCuenta(@RequestBody CrearCuentaRequest request) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(crearCuentaUseCase.ejecutar(request));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Listar cuentas por usuario
    @GetMapping("/listar")
    public ResponseEntity<List<CuentaResponse>> listar(
            @RequestParam UUID idUsuario) {

        try {
            return ResponseEntity.ok(listarCuentasPorUsuarioUseCase.ejecutar(idUsuario));

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
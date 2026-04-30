package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.MovimientosCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenCategoriasResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.ResumenGastosCategorias;
import com.fabrica.gestionfinancierapersonal.application.usecases.TransaccionesPorCategoria;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ResumenGastosCategorias resumenGastosCategorias;
    private final TransaccionesPorCategoria obtenerTransaccionesPorCategoria;

    public ReportesController(ResumenGastosCategorias resumenGastosCategorias,
            TransaccionesPorCategoria obtenerTransaccionesPorCategoria) {
        this.resumenGastosCategorias = resumenGastosCategorias;
        this.obtenerTransaccionesPorCategoria = obtenerTransaccionesPorCategoria;
    }

    // Obtener resumen de categorias general de una cuenta
    @GetMapping("/resumenCategorias")
    public ResponseEntity<List<ResumenCategoriasResponse>> resumenGastos(
            @RequestParam UUID cuentaId,
            @RequestParam UUID usuarioId) {
        try {
            return ResponseEntity.ok(
                    resumenGastosCategorias.ejecutar(cuentaId, usuarioId));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Obtener transacciones de una cuenta por categoria
    @GetMapping("/transaccionesCategoria")
    public ResponseEntity<List<MovimientosCategoriaResponse>> obtenerPorCategoria(
            @RequestParam UUID cuentaId,
            @RequestParam UUID categoriaId,
            @RequestParam UUID usuarioId) {
        try {
            return ResponseEntity.ok(
                    obtenerTransaccionesPorCategoria.ejecutar(cuentaId, categoriaId, usuarioId));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

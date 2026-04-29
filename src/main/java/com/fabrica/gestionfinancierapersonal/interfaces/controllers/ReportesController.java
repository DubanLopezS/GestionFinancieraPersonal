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

import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenCategoriasResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.ResumenGastosCategorias;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ResumenGastosCategorias resumenGastosCategorias;

    public ReportesController(ResumenGastosCategorias resumenGastosCategorias) {
        this.resumenGastosCategorias = resumenGastosCategorias;
    }

    @GetMapping("/resumenCategorias")
    public ResponseEntity<List<ResumenCategoriasResponse>> resumenGastos(
            @RequestParam UUID cuentaId) {
        try {
            return ResponseEntity.ok(resumenGastosCategorias.ejecutar(cuentaId));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

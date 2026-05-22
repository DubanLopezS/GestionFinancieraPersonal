package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.MayorGastoCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.MovimientosCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenCategoriasResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenFinancieroResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransaccionCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.ConsultarResumenFinanciero;
import com.fabrica.gestionfinancierapersonal.application.usecases.ListarGastosPorCategoria;
import com.fabrica.gestionfinancierapersonal.application.usecases.ObtenerCategoriaMayorGasto;
import com.fabrica.gestionfinancierapersonal.application.usecases.ResumenGastosCategorias;
import com.fabrica.gestionfinancierapersonal.application.usecases.TransaccionesPorCategoria;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ResumenGastosCategorias resumenGastosCategorias;
    private final TransaccionesPorCategoria obtenerTransaccionesPorCategoria;
    private final ConsultarResumenFinanciero consultarResumenFinanciero;
    private final ObtenerCategoriaMayorGasto obtenerCategoriaMayorGasto;
    private final ListarGastosPorCategoria listarGastosPorCategoria;

    public ReportesController(ResumenGastosCategorias resumenGastosCategorias,
            TransaccionesPorCategoria obtenerTransaccionesPorCategoria,
            ConsultarResumenFinanciero consultarResumenFinanciero,
            ObtenerCategoriaMayorGasto obtenerCategoriaMayorGasto,
            ListarGastosPorCategoria listarGastosPorCategoria) {
        this.resumenGastosCategorias = resumenGastosCategorias;
        this.obtenerTransaccionesPorCategoria = obtenerTransaccionesPorCategoria;
        this.consultarResumenFinanciero = consultarResumenFinanciero;
        this.obtenerCategoriaMayorGasto = obtenerCategoriaMayorGasto;
        this.listarGastosPorCategoria = listarGastosPorCategoria;
    }

    // Obtener resumen de categorias general de una cuenta
    @GetMapping("/resumenCategorias")
    public ResponseEntity<List<ResumenCategoriasResponse>> resumenGastos(
            @RequestParam UUID idCuenta,
            @RequestParam UUID idUsuario) {
        try {
            return ResponseEntity.ok(
                    resumenGastosCategorias.ejecutar(idCuenta, idUsuario));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Obtener transacciones de una cuenta por categoria
    @GetMapping("/transaccionesCategoria")
    public ResponseEntity<List<MovimientosCategoriaResponse>> obtenerPorCategoria(
            @RequestParam UUID idCuenta,
            @RequestParam UUID idCategoria,
            @RequestParam UUID idUsuario) {
        try {
            return ResponseEntity.ok(
                    obtenerTransaccionesPorCategoria.ejecutar(idCuenta, idCategoria, idUsuario));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Obtener resumen financiero general del mes
    @GetMapping("/resumenFinanciero")
    public ResponseEntity<ResumenFinancieroResponse> consultarResumen(
            @RequestParam UUID idUsuario) {
        return ResponseEntity.ok(
                consultarResumenFinanciero.ejecutar(idUsuario));
    }

    // Obtener la categoría con mayor gasto del mes
    @GetMapping("/mayorGasto")
    public ResponseEntity<MayorGastoCategoriaResponse> obtenerCategoriaMayorGasto(
            @RequestParam UUID idUsuario) {
        Optional<MayorGastoCategoriaResponse> response = obtenerCategoriaMayorGasto.ejecutar(idUsuario);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response.get());
    }

    // Obtener gastos de una categoría
    @GetMapping("/gastosCategoria")
    public ResponseEntity<List<TransaccionCategoriaResponse>> obtenerGastosCategoria(
            @RequestParam UUID idUsuario,
            @RequestParam UUID idCategoria) {
        return ResponseEntity.ok(
                listarGastosPorCategoria.ejecutar(idUsuario, idCategoria));
    }
}

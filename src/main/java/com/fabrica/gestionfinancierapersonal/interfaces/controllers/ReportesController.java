package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fabrica.gestionfinancierapersonal.application.dtos.ComparacionGastosMesesResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.MayorGastoCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.MovimientosCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenCategoriasResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.ResumenFinancieroResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransaccionCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.CompararGastosMensuales;
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
        private final CompararGastosMensuales compararGastosMensuales;

        public ReportesController(ResumenGastosCategorias resumenGastosCategorias,
                        TransaccionesPorCategoria obtenerTransaccionesPorCategoria,
                        ConsultarResumenFinanciero consultarResumenFinanciero,
                        ObtenerCategoriaMayorGasto obtenerCategoriaMayorGasto,
                        ListarGastosPorCategoria listarGastosPorCategoria,
                        CompararGastosMensuales compararGastosMensuales) {
                this.resumenGastosCategorias = resumenGastosCategorias;
                this.obtenerTransaccionesPorCategoria = obtenerTransaccionesPorCategoria;
                this.consultarResumenFinanciero = consultarResumenFinanciero;
                this.obtenerCategoriaMayorGasto = obtenerCategoriaMayorGasto;
                this.listarGastosPorCategoria = listarGastosPorCategoria;
                this.compararGastosMensuales = compararGastosMensuales;
        }

        // Obtener resumen de categorias general de una cuenta
        @GetMapping("/resumenCategorias")
        public ResponseEntity<List<ResumenCategoriasResponse>> resumenGastos(
                        @RequestParam UUID idCuenta,
                        @RequestParam UUID idUsuario) {
                return ResponseEntity.ok(
                                resumenGastosCategorias.ejecutar(idCuenta, idUsuario));
        }

        // Obtener transacciones de una cuenta por categoria
        @GetMapping("/transaccionesCategoria")
        public ResponseEntity<List<MovimientosCategoriaResponse>> obtenerPorCategoria(
                        @RequestParam UUID idCuenta,
                        @RequestParam UUID idCategoria,
                        @RequestParam UUID idUsuario) {
                return ResponseEntity.ok(
                                obtenerTransaccionesPorCategoria.ejecutar(idCuenta, idCategoria, idUsuario));
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
                return obtenerCategoriaMayorGasto.ejecutar(idUsuario)
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.noContent().build());
        }

        // Obtener gastos de una categoría
        @GetMapping("/gastosCategoria")
        public ResponseEntity<List<TransaccionCategoriaResponse>> obtenerGastosCategoria(
                        @RequestParam UUID idUsuario,
                        @RequestParam UUID idCategoria) {
                return ResponseEntity.ok(
                                listarGastosPorCategoria.ejecutar(idUsuario, idCategoria));
        }

        // Comparar gastos del mes actual con el mes anterior
        @GetMapping("/compararMeses")
        public ResponseEntity<ComparacionGastosMesesResponse> compararGastos(
                        @RequestParam UUID idUsuario) {
                return ResponseEntity.ok(
                                compararGastosMensuales.ejecutar(idUsuario));
        }
}

package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fabrica.gestionfinancierapersonal.application.dtos.ConsultarPresupuestoResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearPresupuestoRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.EditarPresupuestoRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.PresupuestoListadoResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.ConsultarPresupuesto;
import com.fabrica.gestionfinancierapersonal.application.usecases.CrearPresupuesto;
import com.fabrica.gestionfinancierapersonal.application.usecases.EditarPresupuesto;
import com.fabrica.gestionfinancierapersonal.application.usecases.ListarPresupuestos;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {

        private final CrearPresupuesto crearPresupuesto;
        private final ConsultarPresupuesto consultarPresupuesto;
        private final EditarPresupuesto editarPresupuesto;
        private final ListarPresupuestos listarPresupuestos;

        public PresupuestoController(CrearPresupuesto crearPresupuesto, ConsultarPresupuesto consultarPresupuesto,
                        EditarPresupuesto editarPresupuesto, ListarPresupuestos listarPresupuestos) {
                this.crearPresupuesto = crearPresupuesto;
                this.consultarPresupuesto = consultarPresupuesto;
                this.editarPresupuesto = editarPresupuesto;
                this.listarPresupuestos = listarPresupuestos;
        }

        // Crear presupuesto
        @PostMapping("/crear")
        public ResponseEntity<String> crearPresupuesto(@RequestBody CrearPresupuestoRequest request) {
                crearPresupuesto.ejecutar(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body("Presupuesto creado correctamente");
        }

        // Consultar presupuesto
        @GetMapping("/consultar")
        public ResponseEntity<ConsultarPresupuestoResponse> consultarPresupuesto(
                        @RequestParam UUID idUsuario,
                        @RequestParam UUID idCategoria) {
                return ResponseEntity.ok(
                                consultarPresupuesto.ejecutar(
                                                idUsuario,
                                                idCategoria));
        }

        // Editar presupuesto
        @PutMapping("/editar")
        public ResponseEntity<String> editarPresupuesto(
                        @RequestBody EditarPresupuestoRequest request) {
                editarPresupuesto.ejecutar(request);
                return ResponseEntity.ok(
                                "Presupuesto actualizado correctamente");
        }

        // Listar presupuestos de un usuario
        @GetMapping("/listar")
        public ResponseEntity<List<PresupuestoListadoResponse>> listarPresupuestos(
                        @RequestParam UUID idUsuario) {
                return ResponseEntity.ok(
                                listarPresupuestos.ejecutar(idUsuario));
        }
}

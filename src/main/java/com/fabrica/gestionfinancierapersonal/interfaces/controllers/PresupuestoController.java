package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.CrearPresupuestoRequest;
import com.fabrica.gestionfinancierapersonal.application.exceptions.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.LimitePresupuestoInvalidoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.PresupuestoDuplicadoException;
import com.fabrica.gestionfinancierapersonal.application.usecases.CrearPresupuesto;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {

    private final CrearPresupuesto crearPresupuesto;

    public PresupuestoController(CrearPresupuesto crearPresupuesto) {
        this.crearPresupuesto = crearPresupuesto;
    }

    // Crear presupuesto
    @PostMapping("/crear")
    public ResponseEntity<String> crearPresupuesto(@RequestBody CrearPresupuestoRequest request) {
        try {
            crearPresupuesto
                    .ejecutar(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Presupuesto creado correctamente");
        } catch (LimitePresupuestoInvalidoException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());

        } catch (UsuarioNoEncontradoException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());

        } catch (CategoriaNoEncontradaException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
                    
        } catch (PresupuestoDuplicadoException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage());
        }
    }
}

package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.CrearCategoria;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/categorias")
public class CategoriaController {

    private final CrearCategoria crearCategoria;

    public CategoriaController(CrearCategoria crearCategoria) {
        this.crearCategoria = crearCategoria;
    }

    // Crear categoría
    @PostMapping("/crear")
    public ResponseEntity<CrearCategoriaResponse> crearCategoria(@RequestBody CrearCategoriaRequest request) {

        System.out.println("REQUEST COMPLETO: " + request);
        System.out.println("NOMBRE: " + request.nombre());
        System.out.println("TIPO: " + request.tipo());

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(crearCategoria.ejecutar(request));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}

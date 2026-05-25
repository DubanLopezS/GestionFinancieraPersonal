package com.fabrica.gestionfinancierapersonal.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fabrica.gestionfinancierapersonal.application.dtos.CategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.usecases.CrearCategoria;
import com.fabrica.gestionfinancierapersonal.application.usecases.ListarCategoriasPorTipo;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/categorias")
public class CategoriaController {

    private final CrearCategoria crearCategoria;
    private final ListarCategoriasPorTipo listarCategoriasPorTipo;

    public CategoriaController(CrearCategoria crearCategoria, ListarCategoriasPorTipo listarCategoriasPorTipo) {
        this.crearCategoria = crearCategoria;
        this.listarCategoriasPorTipo = listarCategoriasPorTipo;
    }

    // Crear categoría
    @PostMapping("/crear")
    public ResponseEntity<CrearCategoriaResponse> crearCategoria(@RequestBody CrearCategoriaRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crearCategoria.ejecutar(request));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias(
            @RequestParam UUID idUsuario,
            @RequestParam String tipo) {

        return ResponseEntity.ok(
                listarCategoriasPorTipo.ejecutar(idUsuario, tipo));
    }
}

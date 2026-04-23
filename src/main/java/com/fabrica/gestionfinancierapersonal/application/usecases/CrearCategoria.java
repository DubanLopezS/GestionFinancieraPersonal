package com.fabrica.gestionfinancierapersonal.application.usecases;

import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CrearCategoria {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearCategoria(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public CrearCategoriaResponse ejecutar(CrearCategoriaRequest request) {

        // Validaciones
        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (request.tipo() == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        if (request.idUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }


        // Convertir STRING → ENUM
        TipoTransaccion tipo;
        try {
            tipo = TipoTransaccion.valueOf(request.tipo().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de transacción inválida");
        }


        Optional<Categoria> existente = categoriaRepository.buscarPorNombre(request.nombre().trim(), request.idUsuario());

        if (existente.isPresent()) {
            throw new IllegalArgumentException("El nombre de la categoría ya está en uso");
        }

        Usuario usuario = usuarioRepository.buscarPorId(request.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        // Crear categoría
        Categoria categoria = new Categoria(
                request.nombre(),
                tipo,
                usuario
            );

        categoriaRepository.guardar(categoria);

        return new CrearCategoriaResponse(
                categoria.getNombre(),
                categoria.getTipo().toString()
            );
    }
}

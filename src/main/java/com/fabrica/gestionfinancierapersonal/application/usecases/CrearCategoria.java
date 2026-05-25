package com.fabrica.gestionfinancierapersonal.application.usecases;

import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.CrearCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.CampoObligatorioException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.NombreCategoriaEnUsoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CrearCategoria {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConvertidorEnums convertidorEnums;

    public CrearCategoria(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository, ConvertidorEnums convertidorEnums) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.convertidorEnums = convertidorEnums;
    }

    public CrearCategoriaResponse ejecutar(CrearCategoriaRequest request) {

        // Validaciones
        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new CampoObligatorioException("El nombre es obligatorio");
        }

        if (request.tipo() == null) {
            throw new CampoObligatorioException("El tipo es obligatorio");
        }

        if (request.idUsuario() == null) {
            throw new CampoObligatorioException("El usuario es obligatorio");
        }


        // Convertir STRING → ENUM
        TipoTransaccion tipo = convertidorEnums.convertirATipoTransaccion(request.tipo());


        Optional<Categoria> existente = categoriaRepository.buscarPorNombre(request.nombre().trim(), request.idUsuario());

        if (existente.isPresent()) {
            throw new NombreCategoriaEnUsoException("El nombre de la categoría ya está en uso");
        }

        Usuario usuario = usuarioRepository.buscarPorId(request.idUsuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));


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

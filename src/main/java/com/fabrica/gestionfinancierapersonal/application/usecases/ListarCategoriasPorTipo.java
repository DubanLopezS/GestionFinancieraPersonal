package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.CategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;

@Service
public class ListarCategoriasPorTipo {

    private final CategoriaRepository categoriaRepository;

    public ListarCategoriasPorTipo(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponse> ejecutar(UUID idUsuario, TipoTransaccion tipo) {

        return categoriaRepository.buscarPorUsuarioYTipo(idUsuario, tipo)
                .stream()
                .map(c -> new CategoriaResponse(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getTipo().name()))
                .toList();
    }
}
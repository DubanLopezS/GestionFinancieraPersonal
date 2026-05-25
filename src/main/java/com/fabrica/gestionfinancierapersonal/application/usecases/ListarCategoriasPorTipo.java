package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.CategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.TipoTransaccion;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

@Service
public class ListarCategoriasPorTipo {

    private final CategoriaRepository categoriaRepository;
    private final ConvertidorEnums convertidorEnums;

    public ListarCategoriasPorTipo(CategoriaRepository categoriaRepository, ConvertidorEnums convertidorEnums) {
        this.categoriaRepository = categoriaRepository;
        this.convertidorEnums = convertidorEnums;
    }

    public List<CategoriaResponse> ejecutar(UUID idUsuario, String tipo) {

        TipoTransaccion tipoEnum = convertidorEnums.convertirATipoTransaccion(tipo);

        return categoriaRepository.buscarPorUsuarioYTipo(idUsuario, tipoEnum)
                .stream()
                .map(c -> new CategoriaResponse(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getTipo().name()))
                .toList();
    }
}
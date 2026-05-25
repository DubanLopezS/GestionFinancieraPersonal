package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.ActualizarTransaccionRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.ActualizarTransaccionResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaInvalidaParaTransaccionException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.transaccion.TransaccionNoExistenteException;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;

@Service
public class ActualizarTransaccion {

    private final TransaccionRepository transaccionRepository;
    private final CategoriaRepository categoriaRepository;

    public ActualizarTransaccion(TransaccionRepository transaccionRepository, CategoriaRepository categoriaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ActualizarTransaccionResponse ejecutar(ActualizarTransaccionRequest request) {

        Transaccion transaccion = transaccionRepository.buscarPorId(request.idTransaccion())
                .orElseThrow(() -> new TransaccionNoExistenteException("Transacción no encontrada"));

        if (!transaccion.getCuenta().getUsuario().getIdUsuario().equals(request.idUsuario())) {
            throw new TransaccionNoExistenteException("La transacción no pertenece al usuario");
        }

        if (request.idCategoria() != null) {

            Categoria categoria = categoriaRepository
                    .buscarPorIdYUsuario(request.idCategoria(), request.idUsuario())
                    .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no válida"));

            if (!categoria.getTipo().equals(transaccion.getTipo())) {
                throw new CategoriaInvalidaParaTransaccionException("Tipo de categoría no coincide");
            }

            // Se actualiza si no tiene categoria o si es diferente
            if (transaccion.getCategoria() == null || 
                !transaccion.getCategoria().getIdCategoria().equals(categoria.getIdCategoria())) {
                
                transaccion.setCategoria(categoria);
                transaccionRepository.guardar(transaccion);
            }

        } else if (transaccion.getCategoria() == null) {
            throw new CategoriaInvalidaParaTransaccionException("La transacción debe tener una categoría válida");
        }

        // Validar que siempre haya categoría después de actualizar
        if (transaccion.getCategoria() == null) {
            throw new CategoriaInvalidaParaTransaccionException("La transacción debe tener una categoría válida");
        }

        return new ActualizarTransaccionResponse(
                transaccion.getIdTransaccion(),
                transaccion.getMonto(),
                transaccion.getTipo().name(),
                transaccion.getCategoria().getNombre());
    }
}

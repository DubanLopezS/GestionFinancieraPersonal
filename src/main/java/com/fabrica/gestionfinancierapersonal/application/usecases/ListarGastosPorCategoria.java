package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.List;
import java.util.UUID;
import com.fabrica.gestionfinancierapersonal.application.dtos.TransaccionCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.categoria.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Transaccion;
import org.springframework.stereotype.Service;

@Service
public class ListarGastosPorCategoria {

        private final UsuarioRepository usuarioRepository;
        private final CategoriaRepository categoriaRepository;
        private final TransaccionRepository transaccionRepository;

        public ListarGastosPorCategoria(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository,
                        TransaccionRepository transaccionRepository) {
                this.usuarioRepository = usuarioRepository;
                this.categoriaRepository = categoriaRepository;
                this.transaccionRepository = transaccionRepository;
        }

        public List<TransaccionCategoriaResponse> ejecutar(UUID idUsuario, UUID idCategoria) {

                usuarioRepository.buscarPorId(idUsuario)
                                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

                Categoria categoria = categoriaRepository.buscarPorId(idCategoria)
                                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

                if (!categoria.getUsuario().getIdUsuario().equals(idUsuario)) {
                        throw new CategoriaNoEncontradaException("La categoría no pertenece al usuario");
                }

                List<Transaccion> transacciones = transaccionRepository.obtenerGastosPorCategoria(idUsuario,
                                idCategoria);

                return transacciones.stream()
                                .map(transaccion -> new TransaccionCategoriaResponse(
                                                transaccion.getIdTransaccion(),
                                                transaccion.getMonto(),
                                                transaccion.getFecha(),
                                                transaccion.getCuenta().getNombre()))
                                .toList();
        }
}
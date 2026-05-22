package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.EditarPresupuestoRequest;
import com.fabrica.gestionfinancierapersonal.application.exceptions.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.LimitePresupuestoInvalidoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.PresupuestoNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.PresupuestoRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.PeriodoPresupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

@Service
public class EditarPresupuesto {

        private final PresupuestoRepository presupuestoRepository;
        private final UsuarioRepository usuarioRepository;
        private final CategoriaRepository categoriaRepository;
        private final ConvertidorEnums convertidorEnums;

        public EditarPresupuesto(
                        PresupuestoRepository presupuestoRepository, UsuarioRepository usuarioRepository,
                        CategoriaRepository categoriaRepository, ConvertidorEnums convertidorEnums) {
                this.presupuestoRepository = presupuestoRepository;
                this.usuarioRepository = usuarioRepository;
                this.categoriaRepository = categoriaRepository;
                this.convertidorEnums = convertidorEnums;
        }

        public void ejecutar(EditarPresupuestoRequest request) {

                if (request.nuevoLimite() <= 0) {
                        throw new LimitePresupuestoInvalidoException("El límite debe ser mayor a 0");
                }

                Usuario usuario = usuarioRepository.buscarPorId(request.idUsuario())
                                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

                Categoria categoria = categoriaRepository.buscarPorId(request.idCategoria())
                                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

                if (!categoria.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                        throw new CategoriaNoEncontradaException("La categoría no pertenece al usuario");
                }

                Presupuesto presupuesto = presupuestoRepository
                                .buscarActivoPorUsuarioYCategoria(request.idUsuario(), request.idCategoria())
                                .orElseThrow(() -> new PresupuestoNoEncontradoException(
                                                "No existe un presupuesto activo"));

                // Convertir STRING a ENUM
                PeriodoPresupuesto nuevoPeriodo = convertidorEnums.convertirAPeriodoPresupuesto(request.nuevoPeriodo());

                // Actualizar presupuesto
                presupuesto.actualizarLimite(
                                request.nuevoLimite());
                // Actualizar periodo solo si cambia
                if (!presupuesto.getPeriodo()
                                .equals(nuevoPeriodo)) {
                        presupuesto.actualizarPeriodo(
                                        nuevoPeriodo);
                }
                presupuestoRepository.guardar(
                                presupuesto);
        }

}

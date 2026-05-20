package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.CrearPresupuestoRequest;
import com.fabrica.gestionfinancierapersonal.application.exceptions.CategoriaNoEncontradaException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.LimitePresupuestoInvalidoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.presupuesto.PresupuestoDuplicadoException;
import com.fabrica.gestionfinancierapersonal.application.repository.CategoriaRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.PresupuestoRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.PeriodoPresupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;
import com.fabrica.gestionfinancierapersonal.domain.model.Presupuesto;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ConvertidorEnums;

@Service
public class CrearPresupuesto {

        private final PresupuestoRepository presupuestoRepository;
        private final UsuarioRepository usuarioRepository;
        private final CategoriaRepository categoriaRepository;
        private final ConvertidorEnums convertidorEnums;

        public CrearPresupuesto(
                        PresupuestoRepository presupuestoRepository,
                        UsuarioRepository usuarioRepository,
                        CategoriaRepository categoriaRepository,
                        ConvertidorEnums convertidorEnums) {

                this.presupuestoRepository = presupuestoRepository;
                this.usuarioRepository = usuarioRepository;
                this.categoriaRepository = categoriaRepository;
                this.convertidorEnums = convertidorEnums;
        }

        public void ejecutar(
                        CrearPresupuestoRequest request) {

                if (request.limite() <= 0) {
                        throw new LimitePresupuestoInvalidoException("El límite debe ser mayor a cero");
                }

                // Buscar usuario y categoría
                Usuario usuario = usuarioRepository.buscarPorId(request.usuarioId())
                                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

                Categoria categoria = categoriaRepository.buscarPorId(request.categoriaId())
                                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

                // Convertir STRING a ENUM
                PeriodoPresupuesto periodo = convertidorEnums.convertirAPeriodoPresupuesto(request.periodo());

                // Validar que no exista un presupuesto activo para esa categoría y período
                boolean existe = presupuestoRepository.existeActivoPorUsuarioCategoriaYPeriodo(
                                usuario.getIdUsuario(),
                                categoria.getIdCategoria(),
                                periodo);

                if (existe) {
                        throw new PresupuestoDuplicadoException(
                                        "Ya existe un presupuesto activo para esta categoría y período");
                }

                LocalDateTime ahora = LocalDateTime.now();

                // Crear presupuesto
                Presupuesto presupuesto = new Presupuesto(
                                usuario,
                                categoria,
                                request.limite(),
                                periodo,
                                ahora);

                // Calcular fecha de expiración del presupuesto según el período
                presupuesto.setFechaExpiracion(
                                periodo
                                                .calcularExpiracion(ahora));

                presupuestoRepository
                                .guardar(presupuesto);
        }
}

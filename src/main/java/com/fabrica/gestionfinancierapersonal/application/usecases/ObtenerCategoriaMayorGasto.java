package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.*;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.MayorGastoCategoriaResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.TransaccionRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.domain.model.Categoria;

@Service
public class ObtenerCategoriaMayorGasto {

    private final UsuarioRepository usuarioRepository;
    private final TransaccionRepository transaccionRepository;

    public ObtenerCategoriaMayorGasto(
            UsuarioRepository usuarioRepository,
            TransaccionRepository transaccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public Optional<MayorGastoCategoriaResponse> ejecutar(UUID idUsuario) {

        usuarioRepository.buscarPorId(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        List<Categoria> categorias = transaccionRepository.obtenerCategoriaMayorGasto(idUsuario);

        if (categorias.isEmpty()) {
            return Optional.empty();
        }

        Categoria categoriaMayor = categorias.get(0);
        double total = transaccionRepository.sumarGastosPorCategoriaDelMes(
                idUsuario,
                categoriaMayor.getIdCategoria());

        return Optional.of(
                new MayorGastoCategoriaResponse(
                        categoriaMayor.getIdCategoria(),
                        categoriaMayor.getNombre(),
                        total));
    }
}

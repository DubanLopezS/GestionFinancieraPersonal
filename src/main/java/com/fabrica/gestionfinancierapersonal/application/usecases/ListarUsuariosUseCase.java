package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.UsuarioListadoResponse;
import com.fabrica.gestionfinancierapersonal.application.exceptions.AccesoDenegadoException;
import com.fabrica.gestionfinancierapersonal.application.exceptions.UsuarioNoEncontradoException;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.enums.Rol;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Service
public class ListarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Page<UsuarioListadoResponse> ejecutar(
            UUID idAdmin,
            String filtro,
            Pageable pageable) {

                
        Usuario admin = usuarioRepository.buscarPorId(idAdmin)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (admin.getRol() != Rol.ADMIN) {
            throw new AccesoDenegadoException("No tienes permisos");
        }

        return usuarioRepository
                .buscarUsuarios(idAdmin, filtro, pageable)
                .map(usuario -> new UsuarioListadoResponse(
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getTelefono(),
                        usuario.getUsername()));
    }
}

package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Service
public class RegistrarUsuario {

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public RegistrarUsuarioResponse ejecutar(RegistrarUsuarioRequest request) {

        // 1. Validaciones básicas (Use Case)
        if (request.getCorreo() == null || request.getCorreo().isBlank()) {
            throw new RuntimeException("Correo obligatorio");
        }

        if (request.getContrasena().length() < 8) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres");
        }

        // 2. Regla del sistema → correo único
        if (usuarioRepository.buscarPorCorreo(request.getCorreo()) != null) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // 3. Crear entidad (dominio)
        String id = UUID.randomUUID().toString();

        Usuario usuario = new Usuario(
                id,
                request.getNombre(),
                request.getCorreo(),
                request.getContrasena(),
                request.getTelefono());

        // 4. Guardar
        usuarioRepository.guardar(usuario);

        // 5. Retornar respuesta
        return new RegistrarUsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo());
    }
}

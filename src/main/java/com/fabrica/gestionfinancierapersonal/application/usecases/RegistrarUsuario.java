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

        // Validar campos obligatorios
        if (esCampoVacio(request.getNombre()) ||
                esCampoVacio(request.getCorreo()) ||
                esCampoVacio(request.getContrasena()) ||
                esCampoVacio(request.getTelefono())) {

            throw new RuntimeException("Todos los campos son obligatorios");
        }

        if (!esCorreoValido(request.getCorreo())) {
            throw new RuntimeException("Formato de correo inválido");
        }

        if (!esContrasenaValida(request.getContrasena())) {
            throw new RuntimeException(
                    "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial");
        }

        if (usuarioRepository.buscarPorCorreo(request.getCorreo()) != null) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Crear entidad (dominio)
        String id = UUID.randomUUID().toString();

        Usuario usuario = new Usuario(
                id,
                request.getNombre(),
                request.getCorreo(),
                request.getContrasena(),
                request.getTelefono());

        // Guardar
        usuarioRepository.guardar(usuario);

        // Retornar respuesta
        return new RegistrarUsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo());
    }

    // Validaciones

    private boolean esCampoVacio(String campo) {
        return campo == null || campo.isBlank();
    }

    private boolean esCorreoValido(String correo) {
        return correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean esContrasenaValida(String contrasena) {
        if (contrasena.length() < 8) {
            return false;
        }

        boolean tieneMayuscula = contrasena.matches(".*[A-Z].*");
        boolean tieneMinuscula = contrasena.matches(".*[a-z].*");
        boolean tieneNumero = contrasena.matches(".*\\d.*");
        boolean tieneEspecial = contrasena.matches(".*[@$!%*?&].*");

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }
}

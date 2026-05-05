package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.RegistrarUsuarioResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorCorreo;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorContrasena;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorNombre;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorTelefono;


@Service
public class RegistrarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidadorCorreo validadorCorreo;
    private final ValidadorContrasena validadorContrasena;
    private final ValidadorNombre validadorNombre;
    private final ValidadorTelefono validadorTelefono;


    public RegistrarUsuario(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ValidadorCorreo validadorCorreo,
        ValidadorContrasena validadorContrasena, ValidadorNombre validadorNombre, ValidadorTelefono validadorTelefono) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.validadorCorreo = validadorCorreo;
        this.validadorContrasena = validadorContrasena;
        this.validadorNombre = validadorNombre;
        this.validadorTelefono = validadorTelefono;
    }

    public RegistrarUsuarioResponse ejecutar(RegistrarUsuarioRequest request) {

        if (usuarioRepository.buscarPorUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("El username ya existe");
        }

        if (usuarioRepository.buscarPorCorreo(request.correo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

         // Validar campos basicos
        validarCamposObligatorios(request);

        // HASH
        String passwordHash = passwordEncoder.encode(request.contrasena());

        // Crear usuario
        Usuario usuario = new Usuario(
                request.username(),
                request.nombre(),
                request.apellido(),
                request.correo(),
                passwordHash,
                request.telefono());

        // Guardar
        usuarioRepository.guardar(usuario);

        // Retornar respuesta
        return new RegistrarUsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo());
    }

    // Validaciones

    private void validarCamposObligatorios(RegistrarUsuarioRequest request) {
        validadorNombre.validar(request.nombre());
        validadorNombre.validar(request.apellido());
        validadorCorreo.validar(request.correo());
        validadorContrasena.validar(request.contrasena());
        validadorTelefono.validar(request.telefono());
    }
}
package com.fabrica.gestionfinancierapersonal.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.LoginUsuarioRequest;
import com.fabrica.gestionfinancierapersonal.application.dtos.LoginUsuarioResponse;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.CredencialesInvalidasException;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorContrasena;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorCorreo;

@Service
public class LoginUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidadorCorreo validadorCorreo;
    private final ValidadorContrasena validadarContrasena;

    
    public LoginUsuario(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ValidadorCorreo validadorCorreo, ValidadorContrasena validadarContrasena) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.validadorCorreo = validadorCorreo;
        this.validadarContrasena = validadarContrasena;
    }

    public LoginUsuarioResponse ejecutar(LoginUsuarioRequest request) {

        // Validar campos básicos
        validadorCorreo.validar(request.correo());
        validadarContrasena.validar(request.contrasena());


        // Buscar usuario
        Usuario usuario = usuarioRepository.buscarPorCorreo(request.correo())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));

                
        // Validad contraseña
        boolean coincide = passwordEncoder.matches(
                request.contrasena(),
                usuario.getContrasena());
        if (!coincide) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        return new LoginUsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre());
    }
}
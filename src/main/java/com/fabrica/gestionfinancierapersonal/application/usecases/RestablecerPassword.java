package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fabrica.gestionfinancierapersonal.application.dtos.RestablecerPasswordRequest;
import com.fabrica.gestionfinancierapersonal.application.repository.RecuperacionPasswordRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.CodigoInvalidoException;
import com.fabrica.gestionfinancierapersonal.domain.exceptions.usuario.ContrasenasNoCoincidenException;
import com.fabrica.gestionfinancierapersonal.domain.model.RecuperacionPassword;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;
import com.fabrica.gestionfinancierapersonal.domain.validators.ValidadorContrasena;

@Service
public class RestablecerPassword {

        private final UsuarioRepository usuarioRepository;
        private final RecuperacionPasswordRepository recuperacionPasswordRepository;
        private final PasswordEncoder passwordEncoder;
        private final ValidadorContrasena validadorContrasena;

        public RestablecerPassword(UsuarioRepository usuarioRepository,
                        RecuperacionPasswordRepository recuperacionPasswordRepository,
                        PasswordEncoder passwordEncoder,
                        ValidadorContrasena validadorContrasena) {
                this.usuarioRepository = usuarioRepository;
                this.recuperacionPasswordRepository = recuperacionPasswordRepository;
                this.passwordEncoder = passwordEncoder;
                this.validadorContrasena = validadorContrasena;
        }

        public void ejecutar(RestablecerPasswordRequest request) {

                RecuperacionPassword recuperacion = recuperacionPasswordRepository
                                .buscarPorCodigo(request.codigo())
                                .orElseThrow(() -> new CodigoInvalidoException("Código de recuperación inválido"));

                if (recuperacion.getFechaExpiracion()
                                .isBefore(LocalDateTime.now())) {
                        throw new CodigoInvalidoException("El código de recuperación ha expirado");
                }

                if (recuperacion.isUsado()) {
                        throw new CodigoInvalidoException("El código ya fue usado");
                }

                if (!request.nuevaPassword()
                                .equals(request.confirmacionPassword())) {
                        throw new ContrasenasNoCoincidenException("Las contraseñas no coinciden");
                }

                // Validar reglas de seguridad
                validadorContrasena.validar(request.nuevaPassword());

                Usuario usuario = recuperacion.getUsuario();
                usuario.setContrasena(
                                passwordEncoder.encode(request.nuevaPassword()));

                usuarioRepository.guardar(usuario);

                recuperacion.setUsado(true);

                recuperacionPasswordRepository
                                .guardar(recuperacion);
        }
}

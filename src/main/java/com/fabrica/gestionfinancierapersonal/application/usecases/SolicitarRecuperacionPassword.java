package com.fabrica.gestionfinancierapersonal.application.usecases;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.fabrica.gestionfinancierapersonal.application.dtos.SolicitarRecuperacionRequest;
import com.fabrica.gestionfinancierapersonal.application.repository.RecuperacionPasswordRepository;
import com.fabrica.gestionfinancierapersonal.application.repository.UsuarioRepository;
import com.fabrica.gestionfinancierapersonal.application.services.EmailService;
import com.fabrica.gestionfinancierapersonal.domain.model.RecuperacionPassword;
import com.fabrica.gestionfinancierapersonal.domain.model.Usuario;

@Service
public class SolicitarRecuperacionPassword {

        private final UsuarioRepository usuarioRepository;
        private final RecuperacionPasswordRepository recuperacionPasswordRepository;
        private final EmailService emailService;

        public SolicitarRecuperacionPassword(UsuarioRepository usuarioRepository,
                        RecuperacionPasswordRepository recuperacionPasswordRepository,
                        EmailService emailService) {
                this.usuarioRepository = usuarioRepository;
                this.recuperacionPasswordRepository = recuperacionPasswordRepository;
                this.emailService = emailService;
        }

        public void ejecutar(
                        SolicitarRecuperacionRequest request) {

                Usuario usuario = usuarioRepository
                                .buscarPorCorreo(request.correo())
                                .orElseThrow(() -> new RuntimeException(
                                                "El correo no está registrado"));

                LocalDateTime ahora = LocalDateTime.now();

                String codigo = String.valueOf(
                                ThreadLocalRandom.current()
                                                .nextInt(100000, 999999));

                RecuperacionPassword recuperacion = new RecuperacionPassword();

                recuperacion.setUsuario(usuario);

                recuperacion.setCodigo(codigo);

                recuperacion.setFechaCreacion(
                                ahora);

                recuperacion.setFechaExpiracion(
                                ahora.plusMinutes(10));

                recuperacion.setUsado(false);

                recuperacionPasswordRepository
                                .guardar(recuperacion);

                emailService.enviarCodigoRecuperacion(
                                usuario.getCorreo(),
                                codigo);
        }
}

package com.fabrica.gestionfinancierapersonal.application.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

        private final JavaMailSender mailSender;

        public EmailService(JavaMailSender mailSender) {
                this.mailSender = mailSender;
        }

        public void enviarCodigoRecuperacion(
                        String correo,
                        String codigo) {

                try {

                        MimeMessage mensaje = mailSender.createMimeMessage();

                        MimeMessageHelper helper = new MimeMessageHelper(
                                        mensaje,
                                        true,
                                        "UTF-8");

                        helper.setFrom(
                                        "gestion.finanzas.app@gmail.com");

                        helper.setTo(correo);

                        helper.setSubject(
                                        "Recuperación de contraseña");

                        String html = """
                                        <div style="
                                            background-color: #f4f6f9;
                                            padding: 40px 20px;
                                            font-family: Arial, sans-serif;
                                        ">

                                            <div style="
                                                max-width: 600px;
                                                margin: auto;
                                                background-color: white;
                                                border-radius: 16px;
                                                overflow: hidden;
                                                box-shadow: 0 4px 15px rgba(0,0,0,0.08);
                                            ">

                                                <!-- HEADER -->
                                                <div style="
                                                    background: linear-gradient(135deg, #1976d2, #42a5f5);
                                                    padding: 30px;
                                                    text-align: center;
                                                    color: white;
                                                ">

                                                    <h1 style="
                                                        margin: 0;
                                                        font-size: 28px;
                                                    ">
                                                        Gestión Financiera
                                                    </h1>

                                                    <p style="
                                                        margin-top: 10px;
                                                        font-size: 15px;
                                                        opacity: 0.9;
                                                    ">
                                                        Recuperación de contraseña
                                                    </p>

                                                </div>

                                                <!-- BODY -->
                                                <div style="
                                                    padding: 40px 35px;
                                                    color: #333333;
                                                ">

                                                    <h2 style="
                                                        margin-top: 0;
                                                        color: #1976d2;
                                                    ">
                                                        Hola
                                                    </h2>

                                                    <p style="
                                                        font-size: 16px;
                                                        line-height: 1.7;
                                                    ">
                                                        Hemos recibido una solicitud para
                                                        restablecer tu contraseña.
                                                    </p>

                                                    <p style="
                                                        font-size: 16px;
                                                        line-height: 1.7;
                                                    ">
                                                        Usa el siguiente código para continuar:
                                                    </p>

                                                    <!-- CODIGO -->
                                                    <div style="
                                                        margin: 35px 0;
                                                        text-align: center;
                                                    ">

                                                        <div style="
                                                            display: inline-block;
                                                            background-color: #f4f8ff;
                                                            border: 2px dashed #1976d2;
                                                            border-radius: 12px;
                                                            padding: 20px 35px;
                                                        ">

                                                            <span style="
                                                                font-size: 38px;
                                                                font-weight: bold;
                                                                letter-spacing: 10px;
                                                                color: #1976d2;
                                                            ">
                                                                """ + codigo + """
                                                            </span>

                                                        </div>

                                                    </div>

                                                    <p style="
                                                        font-size: 15px;
                                                        color: #666666;
                                                        line-height: 1.6;
                                                    ">
                                                        Este código expirará en
                                                        <strong>10 minutos</strong>.
                                                    </p>

                                                    <p style="
                                                        font-size: 15px;
                                                        color: #666666;
                                                        line-height: 1.6;
                                                    ">
                                                        Si no solicitaste este cambio,
                                                        puedes ignorar este mensaje.
                                                    </p>

                                                </div>

                                                <!-- FOOTER -->
                                                <div style="
                                                    background-color: #f8f9fa;
                                                    padding: 20px;
                                                    text-align: center;
                                                    font-size: 13px;
                                                    color: #888888;
                                                    border-top: 1px solid #eeeeee;
                                                ">

                                                    © 2026 Gestión Financiera Personal

                                                </div>

                                            </div>

                                        </div>
                                        """;

                        helper.setText(html, true);

                        mailSender.send(mensaje);

                } catch (MessagingException e) {

                        throw new RuntimeException(
                                        "Error enviando correo", e);
                }
        }
}

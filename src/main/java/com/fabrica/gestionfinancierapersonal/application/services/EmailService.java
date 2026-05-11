package com.fabrica.gestionfinancierapersonal.application.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    private final RestClient restClient;

    public EmailService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(
                "https://api.resend.com").build();
    }

    public void enviarCodigoRecuperacion(
            String correo,
            String codigo) {

        Map<String, Object> body = Map.of(
                "from", "onboarding@resend.dev",
                "to", correo,
                "subject", "Recuperación de contraseña",
                "html",
                "<h2>Recuperación de contraseña</h2>"
                        + "<p>Tu código es:</p>"
                        + "<h1>" + codigo + "</h1>"
                        + "<p>Este código expira en 10 minutos.</p>");

        restClient.post()
                .uri("/emails")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
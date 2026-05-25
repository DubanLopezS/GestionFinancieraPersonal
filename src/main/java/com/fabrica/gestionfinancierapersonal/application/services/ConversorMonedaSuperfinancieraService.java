package com.fabrica.gestionfinancierapersonal.application.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fabrica.gestionfinancierapersonal.application.dtos.ConversionMonedaResponse;
import com.fabrica.gestionfinancierapersonal.domain.enums.Moneda;

@Service
public class ConversorMonedaSuperfinancieraService implements ConversorMonedaService {

    @Override
    public ConversionMonedaResponse convertir(double monto, Moneda monedaOrigen, Moneda monedaDestino) {

        if (monedaOrigen == monedaDestino) {
            return new ConversionMonedaResponse(
                    monto,
                    1);
        }

        double trm = obtenerTRM();

        if (monedaOrigen == Moneda.COP
                && monedaDestino == Moneda.USD) {
            return new ConversionMonedaResponse(
                    monto / trm,
                    trm);
        }

        if (monedaOrigen == Moneda.USD
                && monedaDestino == Moneda.COP) {
            return new ConversionMonedaResponse(
                    monto * trm,
                    trm);
        }

        throw new RuntimeException("Conversión no soportada");
    }

    private double obtenerTRM() {

        String soapBody = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:act="http://action.trm.services.generic.action.superfinanciera.nexura.sc.com.co/">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <act:queryTCRM/>
                    </soapenv:Body>
                </soapenv:Envelope>
                """;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        HttpEntity<String> request = new HttpEntity<>(soapBody, headers);

        String url = "https://www.superfinanciera.gov.co/" +
                "SuperfinancieraWebServiceTRM/" +
                "TCRMServicesWebService/" +
                "TCRMServicesWebService";

        String response = restTemplate.postForObject(
                url,
                request,
                String.class);

        Pattern pattern = Pattern.compile("<value>(.*?)</value>");
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        throw new RuntimeException("No fue posible obtener la TRM");
    }
}
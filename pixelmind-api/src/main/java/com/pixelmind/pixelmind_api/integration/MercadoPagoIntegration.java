package com.pixelmind.pixelmind_api.integration;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MercadoPagoIntegration {

    private final RestTemplate restTemplate;
    private final String accessToken = "APP_USR-8516330672660159-071723-b723f03b65b91f3e0158b96ace904ed2-636168041";

    public MercadoPagoIntegration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> criarPagamento(Map<String, Object> payload) {
        String url = "https://api.mercadopago.com/v1/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace(); // mostra no console
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage(), e);
        }

    }
}

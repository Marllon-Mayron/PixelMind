package com.pixelmind.pixelmind_api.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate;
    private final InterAuthService authService;

    public WebhookService(InterAuthService authService) {
        this.authService = authService;
        this.restTemplate = new RestTemplate();
    }

    public void registerWebhook(String webhookUrl) {
        String accessToken = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("url", webhookUrl);
        body.put("events", List.of("PAYMENT_RECEIVED", "PIX_RECEIVED"));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.put("https://api.bancointer.com.br/webhook/v1/config", request);
    }
}


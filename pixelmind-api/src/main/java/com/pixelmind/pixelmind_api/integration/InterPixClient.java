package com.pixelmind.pixelmind_api.integration;

import com.pixelmind.pixelmind_api.service.InterAuthService;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class InterPixClient {

    private final RestTemplate restTemplate;
    private final InterAuthService interAuthService;

    private final String pixUrl = "https://cdpj.partners.bancointer.com.br/openbanking/v1/cob";

    public InterPixClient(RestTemplate restTemplate, InterAuthService interAuthService) {
        this.restTemplate = restTemplate;
        this.interAuthService = interAuthService;
    }

    public Map<String, Object> criarCobrancaPix(Map<String, Object> payload) {
        String accessToken = interAuthService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                pixUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody();
    }

    public String consultarStatusPix(String txid) {
        String accessToken = interAuthService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                pixUrl + "/" + txid,
                HttpMethod.GET,
                request,
                String.class
        );

        return response.getBody();
    }
}


package com.pixelmind.pixelmind_api.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class InterAuthService {

    private final RestTemplate restTemplate;

    public InterAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", "182bd8af-342d-42c3-9bc8-8eb616fe1cf5");
        map.add("client_secret", "7d306c3a-33d3-4ed2-8a76-f8ed99f7ecea");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth.bancointer.com.br/oauth/token",
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        return (String) body.get("access_token");
    }
}



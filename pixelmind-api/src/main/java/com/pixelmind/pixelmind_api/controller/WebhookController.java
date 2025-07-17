package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/inter")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/register-webhook")
    public ResponseEntity<String> registerWebhook() {
        String webhookUrl = "https://f5a0455e1f6b.ngrok-free.app/api/webhook/payment"; // sua URL ngrok
        webhookService.registerWebhook(webhookUrl);
        return ResponseEntity.ok("Webhook registrado com sucesso");
    }
}


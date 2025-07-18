/**
 *
 * package com.pixelmind.pixelmind_api.controller;
 *
 * import com.pixelmind.pixelmind_api.service.WebhookService;
 * import org.springframework.http.ResponseEntity;
 * import org.springframework.web.bind.annotation.*;
 *
 * import java.util.Map;
 *
 * @RestController
 * @RequestMapping("/api/mercadopago")
 * public class WebhookController {
 *
 *     private final WebhookService webhookService;
 *
 *     public WebhookController(WebhookService webhookService) {
 *         this.webhookService = webhookService;
 *     }
 *
 *     @PostMapping("/webhook")
 *     public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> payload) {
 *         webhookService.processWebhook(payload);
 *         return ResponseEntity.ok().build();
 *     }
 * }**/
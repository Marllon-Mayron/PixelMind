package com.pixelmind.pixelmind_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class PixWebhookController {

    @PostMapping("/payment")
    public ResponseEntity<Void> receivePixPayment(@RequestBody Map<String, Object> payload) {
        System.out.println("Recebido webhook Pix: " + payload);

        // Aqui você processa o payload, atualiza pedido no banco etc.

        return ResponseEntity.ok().build();
    }
}

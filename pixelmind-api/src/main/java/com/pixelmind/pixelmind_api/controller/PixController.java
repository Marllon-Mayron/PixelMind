package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.OrderRequestDTO;
import com.pixelmind.pixelmind_api.model.PixPayloadGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class PixController {

    private static final String PIX_KEY = "11931328439";
    private static final String PIX_NAME = "Marllon Silva";
    private static final String CITY = "Recife";

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDTO request) throws Exception {
        // 1. Criar pedido no banco - aqui só simulando
        String orderId = UUID.randomUUID().toString();

        // 2. Gerar payload Pix seguindo o padrão EMV, incluindo chave, valor, infoAdicional (orderId)
        String valor = String.format(Locale.US, "%.2f", request.getAmount());


        String pixPayload = PixPayloadGenerator.generatePayload(
                PIX_KEY,
                PIX_NAME,
                CITY,
                valor,
                orderId
        );

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("pixPayload", pixPayload);

        return ResponseEntity.ok(response);
    }
}


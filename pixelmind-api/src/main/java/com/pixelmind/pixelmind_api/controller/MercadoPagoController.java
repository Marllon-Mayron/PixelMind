package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.OrderRequestDTO;
import com.pixelmind.pixelmind_api.integration.MercadoPagoIntegration;
import com.pixelmind.pixelmind_api.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/mercadopago")
public class MercadoPagoController {

    private final MercadoPagoIntegration mercadoPagoClient;
    private final PurchaseOrderService purchaseOrderService;

    public MercadoPagoController(MercadoPagoIntegration mercadoPagoClient, PurchaseOrderService purchaseOrderService) {
        this.mercadoPagoClient = mercadoPagoClient;
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/createPayment")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody OrderRequestDTO request) {
        String valor = String.format(Locale.US, "%.2f", request.getAmount());

        Map<String, Object> paymentPayload = new HashMap<>();
        paymentPayload.put("transaction_amount", request.getAmount());
        paymentPayload.put("description", "Compra PixelMind NFT");
        paymentPayload.put("payment_method_id", "pix"); // pix como método
        paymentPayload.put("payer", Map.of(
                "email", "comprador@example.com"  // ideal pegar do request ou banco
        ));

        Map<String, Object> response = mercadoPagoClient.criarPagamento(paymentPayload);

        // Salvar pedido e resposta como quiser
        purchaseOrderService.createOrder(request.getUserId(), request.getNftItemId(), request.getAmount(), "MP-" + System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> body) {
        // Tratar a notificação do Mercado Pago
        System.out.println("Webhook recebido: " + body);

        // Aqui você pega o ID da transação e atualiza no banco
        // Exemplo (precisa adaptar para o formato do Mercado Pago)
        // String paymentId = (String) body.get("id");
        // purchaseOrderService.updateStatusPorPagamentoId(paymentId, "PAID");

        return ResponseEntity.ok().build();
    }
}


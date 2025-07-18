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

    @PostMapping(value = "/webhook", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<Void> handleWebhook(@RequestBody(required = false) Map<String, Object> body,
                                              @RequestParam(required = false) Map<String, String> formParams) {
        // Para debug
        System.out.println("Webhook recebido:");
        if (body != null) {
            System.out.println("JSON: " + body);
        }
        if (formParams != null) {
            System.out.println("FORM: " + formParams);
        }

        // Acessar ID da transação
        String paymentId = null;

        if (body != null && body.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            paymentId = String.valueOf(data.get("id"));
        } else if (formParams != null && formParams.containsKey("data.id")) {
            paymentId = formParams.get("data.id");
        }

        if (paymentId != null) {
            System.out.println("Pagamento ID recebido no webhook: " + paymentId);

            // Atualiza status no banco com base no ID

        }

        return ResponseEntity.ok().build();
    }

}


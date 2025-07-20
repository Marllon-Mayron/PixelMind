package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.OrderRequestDTO;
import com.pixelmind.pixelmind_api.dto.payment.WebhookPayloadDTO;
import com.pixelmind.pixelmind_api.integration.MercadoPagoIntegration;
import com.pixelmind.pixelmind_api.service.MercadoPagoService;
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
    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoIntegration mercadoPagoClient, PurchaseOrderService purchaseOrderService, MercadoPagoService mercadoPagoService) {
        this.mercadoPagoClient = mercadoPagoClient;
        this.purchaseOrderService = purchaseOrderService;
        this.mercadoPagoService = mercadoPagoService;
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
    public ResponseEntity<Void> handleWebhook(@RequestBody(required = false) WebhookPayloadDTO payload,
                                              @RequestParam(required = false) Map<String, String> formParams) {

        System.out.println("🔔 Webhook recebido:");

        if (payload != null) {
            System.out.println("✅ JSON Payload: " + payload);
            if (payload.getData() != null && payload.getData().getId() != null) {
                Long paymentId = payload.getData().getId();
                System.out.println("💰 Pagamento ID: " + paymentId);
                mercadoPagoService.getPaymentDetails(paymentId);
            }
        } else if (formParams != null && formParams.containsKey("data.id")) {
            Long paymentId = Long.valueOf(formParams.get("data.id"));
            System.out.println("💰 Pagamento ID via FORM: " + paymentId);
            mercadoPagoService.getPaymentDetails(paymentId);
        }

        return ResponseEntity.ok().build();
    }

}


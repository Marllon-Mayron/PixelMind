package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.config.QRCodeGenerator;
import com.pixelmind.pixelmind_api.dto.OrderRequestDTO;
import com.pixelmind.pixelmind_api.model.PixPayloadGenerator;
import com.pixelmind.pixelmind_api.model.store.PurchaseOrder;
import com.pixelmind.pixelmind_api.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class PixController {

    private static final String PIX_KEY = "11931328439";
    private static final String PIX_NAME = "Marllon Silva";
    private static final String CITY = "Recife";

    private final PurchaseOrderService purchaseOrderService;

    public PixController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDTO request) throws Exception {
        String transactionId = UUID.randomUUID().toString().substring(0, 15).replace("-", "");
        String valor = String.format(Locale.US, "%.2f", request.getAmount());

        String pixPayload = PixPayloadGenerator.generatePayload(
                PIX_KEY,
                PIX_NAME,
                CITY,
                valor,
                transactionId
        );

        purchaseOrderService.createOrder(
                request.getUserId(),
                request.getNftItemId(),
                request.getAmount(),
                transactionId
        );

        String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(pixPayload, 300, 300);

        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", transactionId);
        response.put("pixPayload", pixPayload);
        response.put("qrCodeBase64", "data:image/png;base64," + qrCodeBase64);
        System.out.println("Payload: " + pixPayload);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> body) {
        String txid = (String) body.get("txid");
        String status = (String) body.get("status");

        if (txid != null && "CONCLUIDA".equalsIgnoreCase(status)) {
            purchaseOrderService.findByTransactionId(txid).ifPresent(order -> {
                purchaseOrderService.updateStatus(order.getId(), "PAID");
            });
        }

        return ResponseEntity.ok().build();
    }
}


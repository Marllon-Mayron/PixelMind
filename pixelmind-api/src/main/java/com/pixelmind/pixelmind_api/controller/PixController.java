package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.OrderRequestDTO;
import com.pixelmind.pixelmind_api.service.MercadoPagoService;
import com.pixelmind.pixelmind_api.service.PurchaseOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class PixController {

    private final MercadoPagoService mercadoPagoService;
    private final PurchaseOrderService purchaseOrderService;

    public PixController(MercadoPagoService mercadoPagoService, PurchaseOrderService purchaseOrderService) {
        this.mercadoPagoService = mercadoPagoService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDTO request, Principal principal) {
        try {
            String txid = UUID.randomUUID().toString().replace("-", "");
            BigDecimal valor = request.getAmount();
            String email = principal.getName();

            Map<String, Object> pagamento = mercadoPagoService.criarPagamentoPix(valor, txid, email);

            Map<String, Object> pointOfInteraction = (Map<String, Object>) pagamento.get("point_of_interaction");
            Map<String, Object> transactionData = (Map<String, Object>) pointOfInteraction.get("transaction_data");
            String qrCode = (String) transactionData.get("qr_code");
            String qrCodeBase64 = (String) transactionData.get("qr_code_base64");

            purchaseOrderService.createOrder(request.getUserId(), request.getNftItemId(), valor, txid);

            return ResponseEntity.ok(Map.of(
                    "transactionId", txid,
                    "qrCode", qrCode,
                    "qrCodeBase64", "data:image/png;base64," + qrCodeBase64
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar pagamento PIX: " + e.getMessage()));
        }
    }
}

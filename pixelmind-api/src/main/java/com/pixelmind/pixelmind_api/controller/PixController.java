package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.store.OrderRequestDTO;
import com.pixelmind.pixelmind_api.dto.payment.CreatePixPaymentRequestDTO;
import com.pixelmind.pixelmind_api.service.MercadoPagoService;
import com.pixelmind.pixelmind_api.service.PurchaseOrderService;
import com.pixelmind.pixelmind_api.service.UserService;
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
    private final UserService userService;

    public PixController(MercadoPagoService mercadoPagoService, PurchaseOrderService purchaseOrderService, UserService userService) {
        this.mercadoPagoService = mercadoPagoService;
        this.purchaseOrderService = purchaseOrderService;
        this.userService =  userService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDTO request, Principal principal) {
        try {
            request.setUserId(userService.getUserByPrincipal(principal).getId());
            String txid = UUID.randomUUID().toString().replace("-", "");
            BigDecimal valor = request.getAmount();
            String email = principal.getName();

            CreatePixPaymentRequestDTO dto = new CreatePixPaymentRequestDTO();
            dto.setTransaction_amount(valor.doubleValue());
            dto.setPayment_method_id("pix");
            dto.setEmail(email);
            dto.setDescription("Compra PixelMind NFT");
            dto.setExternal_reference(txid);

            Map<String, Object> pagamento = mercadoPagoService.criarPagamentoPix(dto);

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

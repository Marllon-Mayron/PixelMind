package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.store.OrderRequestDTO;
import com.pixelmind.pixelmind_api.model.store.PurchaseOrder;
import com.pixelmind.pixelmind_api.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    // 🔹 Lista todos os pedidos
    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    // 🔹 Busca um pedido por transactionId
    @GetMapping("/{transactionId}")
    public ResponseEntity<PurchaseOrder> getByTransactionId(@PathVariable String transactionId) {
        return service.findByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Cria um novo pedido
    @PostMapping("/create")
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody OrderRequestDTO data) {
        PurchaseOrder order = service.createOrder(
                data.getUserId(),
                data.getNftItemId(),
                data.getAmount(),
                data.getTransactionId()
        );
        return ResponseEntity.ok(order);
    }

    // 🔹 Atualiza o status de um pedido
    @PutMapping("/{orderId}/status")
    public ResponseEntity<PurchaseOrder> updateStatus(@PathVariable Long orderId,
                                                      @RequestParam String status) {
        PurchaseOrder updated = service.updateStatus(orderId, status);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Gera QR Code Pix via Banco Inter
    @PostMapping("/{orderId}/pix")
    public ResponseEntity<String> gerarPix(@PathVariable Long orderId) {
        String qrCode = service.gerarPix(orderId);
        return ResponseEntity.ok(qrCode);
    }

    // 🔹 Consulta status da cobrança PIX
    @GetMapping("/{orderId}/pix/status")
    public ResponseEntity<String> consultarStatusPix(@PathVariable Long orderId) {
        String status = service.consultarStatusPix(orderId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> verificarStatusPagamento(@PathVariable Long orderId) {
        boolean pago = service.verificarPagamento(orderId);
        return ResponseEntity.ok(pago ? "PAGO" : "PENDENTE");
    }


}

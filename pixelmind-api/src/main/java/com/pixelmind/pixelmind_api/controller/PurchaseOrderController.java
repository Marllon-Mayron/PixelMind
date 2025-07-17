package com.pixelmind.pixelmind_api.controller;

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

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PurchaseOrder> getByTransactionId(@PathVariable String transactionId) {
        return service.findByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<PurchaseOrder> createOrder(@RequestParam Long userId,
                                                     @RequestParam Long nftItemId,
                                                     @RequestParam double amount,
                                                     @RequestParam String transactionId) {
        PurchaseOrder order = service.createOrder(userId, nftItemId, amount, transactionId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<PurchaseOrder> updateStatus(@PathVariable Long orderId,
                                                      @RequestParam String status) {
        PurchaseOrder updated = service.updateStatus(orderId, status);
        return ResponseEntity.ok(updated);
    }
}

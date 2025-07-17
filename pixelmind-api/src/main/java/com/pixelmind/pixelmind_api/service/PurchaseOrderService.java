package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.model.store.PurchaseOrder;
import com.pixelmind.pixelmind_api.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository repository;

    public PurchaseOrderService(PurchaseOrderRepository repository) {
        this.repository = repository;
    }

    public PurchaseOrder createOrder(Long userId, Long nftItemId,
                                     double amount, String transactionId) {
        PurchaseOrder order = new PurchaseOrder();
        order.setUserId(userId);
        order.setNftItemId(nftItemId);
        order.setAmount(BigDecimal.valueOf(amount));
        order.setStatus("PENDING");
        order.setTransactionId(transactionId);
        order.setCreatedAt(LocalDateTime.now());
        return repository.save(order);
    }

    public Optional<PurchaseOrder> findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    public List<PurchaseOrder> listAll() {
        return repository.findAll();
    }

    public PurchaseOrder updateStatus(Long orderId, String status) {
        PurchaseOrder order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        order.setStatus(status);

        if ("PAID".equalsIgnoreCase(status)) {
            order.setPaidAt(LocalDateTime.now());
        }

        return repository.save(order);
    }
}

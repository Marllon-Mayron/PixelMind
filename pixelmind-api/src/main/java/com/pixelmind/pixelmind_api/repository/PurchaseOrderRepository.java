package com.pixelmind.pixelmind_api.repository;


import com.pixelmind.pixelmind_api.model.store.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByTransactionId(String transactionId);
}
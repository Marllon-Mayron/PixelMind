package com.pixelmind.pixelmind_api.model.store;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID do usuário que fez o pedido (pode ser uma relação ManyToOne com User, se preferir)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ID do item comprado
    @Column(name = "nft_item_id", nullable = false)
    private Long nftItemId;

    // Valor pago no pedido
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // Status do pedido (exemplo: PENDING, PAID, CANCELLED)
    @Column(nullable = false, length = 20)
    private String status;

    // Identificador único da transação pix (txid)
    @Column(name = "transaction_id", nullable = false, unique = true, length = 36)
    private String transactionId;

    // Data e hora do pedido
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Data e hora da confirmação de pagamento (pode ser null antes do pagamento)
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

}

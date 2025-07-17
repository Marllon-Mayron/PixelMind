package com.pixelmind.pixelmind_api.model.store;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "store_items")
public class StoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com NFT base
    @ManyToOne
    @JoinColumn(name = "nft_item_id", nullable = false)
    private NftItem nftItem;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "on_promotion", nullable = false)
    private boolean onPromotion = false;

    @Column(name = "promotional_price", precision = 12, scale = 2)
    private BigDecimal promotionalPrice;

    @Column(name = "promotion_start")
    private LocalDateTime promotionStart;

    @Column(name = "promotion_end")
    private LocalDateTime promotionEnd;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}

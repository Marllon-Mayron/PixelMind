package com.pixelmind.pixelmind_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StoreItemDTO {
    private Long id;
    private Long nftItemId;
    private BigDecimal price;
    private boolean onPromotion;
    private BigDecimal promotionalPrice;
    private LocalDateTime promotionStart;
    private LocalDateTime promotionEnd;
    private String status;
    private LocalDateTime createdAt;
}

package com.pixelmind.pixelmind_api.dto.store;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StorePromotionDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Integer minQuantity;
    private BigDecimal discountPercentage;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private List<Long> storeItemIds; // IDs dos StoreItems participantes
}

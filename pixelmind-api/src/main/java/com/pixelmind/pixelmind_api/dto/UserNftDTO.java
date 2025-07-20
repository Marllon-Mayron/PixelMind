package com.pixelmind.pixelmind_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserNftDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private boolean forSale;
    private Long collectionId;
    private String tier;
    private Long ownerId;
    private LocalDateTime earnedAt;
}


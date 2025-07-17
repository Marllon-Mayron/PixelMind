package com.pixelmind.pixelmind_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDTO {
    private Long userId;
    private Long nftItemId;
    private BigDecimal amount;
    private String transactionId;
}

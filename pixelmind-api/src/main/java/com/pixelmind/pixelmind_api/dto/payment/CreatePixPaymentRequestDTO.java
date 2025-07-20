package com.pixelmind.pixelmind_api.dto.payment;

import lombok.Data;

@Data
public class CreatePixPaymentRequestDTO {
    private Double transaction_amount;
    private String description;
    private String payment_method_id;
    private String email;
    private String external_reference;
}

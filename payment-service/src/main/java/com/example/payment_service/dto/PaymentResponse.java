package com.example.payment_service.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentResponse(
        Long orderId,
        String status,
        BigDecimal amount,
        String transactionId
) {
}

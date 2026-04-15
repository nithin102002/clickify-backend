package com.example.order_service.dto;

import lombok.Builder;

@Builder
public record PaymentResponse(
        Long orderId,
        String status,
        String transactionId
) {
}

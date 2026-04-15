package com.example.order_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record PaymentRequest(
        Long orderId,
        BigDecimal amount,
        List<OrderItemRequest> items
) {
}

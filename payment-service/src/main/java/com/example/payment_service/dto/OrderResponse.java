package com.example.payment_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        String username,
        List<OrderItemResponse> items,
        BigDecimal totalPrice,
        String status
) {}

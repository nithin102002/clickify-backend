package com.example.payment_service.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity,
        BigDecimal subtotal
) {}
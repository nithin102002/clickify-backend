package com.example.graphql_gateway.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        String status,
        BigDecimal totalPrice
) {
}

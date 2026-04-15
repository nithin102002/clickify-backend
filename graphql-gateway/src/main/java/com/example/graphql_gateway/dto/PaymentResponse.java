package com.example.graphql_gateway.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        String status,
        BigDecimal amount
) {
}

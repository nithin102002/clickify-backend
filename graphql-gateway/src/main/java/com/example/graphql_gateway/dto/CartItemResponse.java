package com.example.graphql_gateway.dto;

public record CartItemResponse(
        Long productId,
        Integer quantity
) {
}

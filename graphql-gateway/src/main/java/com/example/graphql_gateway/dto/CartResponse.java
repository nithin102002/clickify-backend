package com.example.graphql_gateway.dto;

import java.util.List;

public record CartResponse(
        String username,
        List<CartItemResponse> items
) {
}

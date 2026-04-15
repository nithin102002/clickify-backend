package com.example.cart_service.dto;

import java.util.List;

public record CartResponse(
        String username,
        List<CartItemResponse> items
) {}

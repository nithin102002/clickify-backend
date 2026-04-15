package com.example.order_service.dto;

public record CartItemResponse(
        Long productId,
        Integer quantity
) {}

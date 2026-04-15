package com.example.cart_service.dto;


public record ProductResponse(
        Long id,
        String name,
        Integer stockQuantity
) {}

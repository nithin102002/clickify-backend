package com.example.review_service.dto;

public record ReviewRequest(
        Long productId,
        Integer rating,
        String comment
) {}

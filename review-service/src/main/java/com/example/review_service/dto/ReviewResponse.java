package com.example.review_service.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long productId,
        String username,
        Integer rating,
        String comment,
        LocalDateTime createdAt

) {
}

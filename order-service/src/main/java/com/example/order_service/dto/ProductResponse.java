package com.example.order_service.dto;


import com.example.order_service.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        String imageUrl,
        Integer stockQuantity,
        ProductStatus productStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt


) {
}

package com.example.product_service.dto;

import com.example.product_service.enums.ApiStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiResponse<T>(
        ApiStatus status,
        String message,
        T data,
        LocalDateTime timestamp
) {
}
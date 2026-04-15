package com.example.review_service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiResponse<T>(
        String status,
        String message,
        T data,
        LocalDateTime timestamp
) {}

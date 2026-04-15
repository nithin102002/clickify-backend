package com.example.userservice.dto;

import com.example.userservice.enums.ApiStatus;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        ApiStatus status,
        String message,
        T data,
        LocalDateTime timestamp
) {
}

package com.example.userservice.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}

package com.example.api_gateway.security;

public record SecurityRule(
        String path,
        String method,
        String role
) {}

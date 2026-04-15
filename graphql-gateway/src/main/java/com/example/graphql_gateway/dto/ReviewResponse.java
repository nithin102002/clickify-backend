package com.example.graphql_gateway.dto;

public record ReviewResponse(
        Integer rating,
        String comment,
        String username
) {}

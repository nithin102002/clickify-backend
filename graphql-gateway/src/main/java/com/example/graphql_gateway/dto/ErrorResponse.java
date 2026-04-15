package com.example.graphql_gateway.dto;

import java.time.LocalDateTime;

public record ErrorResponse(

        String errorCode,
        String message,
        LocalDateTime timestamp
) {
}

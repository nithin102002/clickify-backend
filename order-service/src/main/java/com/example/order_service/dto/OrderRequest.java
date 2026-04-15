package com.example.order_service.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
        String paymentMode

) {}
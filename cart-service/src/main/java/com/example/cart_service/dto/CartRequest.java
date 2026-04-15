package com.example.cart_service.dto;

import java.util.List;

public record CartRequest(
        List<CartItemRequest> items
) {}

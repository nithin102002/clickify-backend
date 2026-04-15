package com.example.cart_service.service;

import com.example.cart_service.dto.CartItemRequest;
import com.example.cart_service.dto.CartResponse;

public interface CartService {
    CartResponse addToCart(String username, CartItemRequest request);

    CartResponse updateCart(String username, CartItemRequest request);

    void removeFromCart(String username, Long productId);

    CartResponse getCart(String username);

    void clearCart(String username);
}

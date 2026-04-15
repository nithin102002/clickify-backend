package com.example.cart_service.controller;

import com.example.cart_service.api.CartApi;
import com.example.cart_service.constants.MessageConstants;
import com.example.cart_service.dto.ApiResponse;
import com.example.cart_service.dto.CartItemRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.enums.ApiStatus;
import com.example.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CartController implements CartApi {

    private final CartService cartService;

    @Override
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            String username,
            CartItemRequest request) {

        CartResponse response = cartService.addToCart(username, request);

        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.CART_ITEM_ADDED)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(
            String username,
            CartItemRequest request) {

        CartResponse response = cartService.updateCart(username, request);

        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.CART_UPDATED)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> removeFromCart(
            String username,
            Long productId) {

        cartService.removeFromCart(username, productId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.CART_ITEM_REMOVED)
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            String username) {

        CartResponse response = cartService.getCart(username);

        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.CART_FETCHED)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> clearCart(
            String username) {

        cartService.clearCart(username);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.CART_CLEARED)
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
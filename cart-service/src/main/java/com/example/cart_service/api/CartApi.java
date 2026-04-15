package com.example.cart_service.api;

import com.example.cart_service.constants.ApiConstants;
import com.example.cart_service.dto.ApiResponse;
import com.example.cart_service.dto.CartItemRequest;
import com.example.cart_service.dto.CartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiConstants.BASIC_API_URL)
public interface CartApi {

    @PostMapping(ApiConstants.CART)
    ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @RequestHeader("X-User") String username,
            @RequestBody CartItemRequest request
    );

    @PutMapping(ApiConstants.CART)
    ResponseEntity<ApiResponse<CartResponse>> updateCart(
            @RequestHeader("X-User") String username,
            @RequestBody CartItemRequest request
    );

    @DeleteMapping(ApiConstants.CART+ "/{productId}")
    ResponseEntity<ApiResponse<Void>> removeFromCart(
            @RequestHeader("X-User") String username,
            @PathVariable Long productId
    );

    @GetMapping(ApiConstants.CART)
    ResponseEntity<ApiResponse<CartResponse>> getCart(
            @RequestHeader("X-User") String username
    );

    @DeleteMapping(ApiConstants.CART)
    ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestHeader("X-User") String username
    );
}

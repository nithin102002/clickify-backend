package com.example.order_service.client;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/api/v1/cart")
    ApiResponse<CartResponse> getCart(
            @RequestHeader("X-User") String username
    );

    @DeleteMapping("/api/v1/cart")
    void clearCart(
            @RequestHeader("X-User") String username
    );
}
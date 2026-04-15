package com.example.payment_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
public interface CartClient {


    @DeleteMapping("/api/v1/cart")
    void clearCart(
            @RequestHeader("X-User") String username
    );
}
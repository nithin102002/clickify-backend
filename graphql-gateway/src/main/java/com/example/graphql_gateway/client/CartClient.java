package com.example.graphql_gateway.client;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/api/v1/cart")
    ApiResponse<CartResponse> getCart(
            @RequestHeader("X-User") String username
    );
}

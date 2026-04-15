package com.example.graphql_gateway.external;


import com.example.graphql_gateway.client.CartClient;
import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.CartResponse;
import com.example.graphql_gateway.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceWrapper {

    private final CartClient cartClient;

    @CircuitBreaker(name = "cartService", fallbackMethod = "getCartFallback")
    public ApiResponse<CartResponse> getCart(String username) {
        return cartClient.getCart(username);
    }

    public ApiResponse<CartResponse> getCartFallback(String username, Throwable ex) {
        throw new ServiceUnavailableException("Cart service unavailable");
    }


}

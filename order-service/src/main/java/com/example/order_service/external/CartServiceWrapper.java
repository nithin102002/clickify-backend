package com.example.order_service.external;

import com.example.order_service.client.CartClient;
import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.CartResponse;
import com.example.order_service.exception.ServiceUnavailableException;
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

    @CircuitBreaker(name = "cartService", fallbackMethod = "clearCartFallback")
    public void clearCart(String username) {
        cartClient.clearCart(username);
    }

    public void clearCartFallback(String username, Throwable ex) {
        throw new ServiceUnavailableException("Cart service unavailable while clearing cart");
    }
}

package com.example.payment_service.external;



import com.example.payment_service.client.CartClient;
import com.example.payment_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceWrapper {

    private final CartClient cartClient;



    @CircuitBreaker(name = "cartService", fallbackMethod = "clearCartFallback")
    public void clearCart(String username) {
        cartClient.clearCart(username);
    }

    public void clearCartFallback(String username, Throwable ex) {
        throw new ServiceUnavailableException("Cart service unavailable while clearing cart");
    }
}

package com.example.payment_service.external;

import com.example.payment_service.client.ProductClient;
import com.example.payment_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceWrapper {

    private final ProductClient productClient;

    @CircuitBreaker(name = "productService", fallbackMethod = "increaseStockFallback")
    public void increaseStock(Long productId, Integer qty) {
        productClient.increaseStock(productId, qty);
    }

    public void increaseStockFallback(Long productId, Integer qty, Throwable ex) {
        throw new ServiceUnavailableException("Product service unavailable for rollback");
    }
}
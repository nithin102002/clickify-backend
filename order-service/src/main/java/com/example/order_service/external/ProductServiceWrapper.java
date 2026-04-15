package com.example.order_service.external;

import com.example.order_service.client.ProductClient;
import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.ProductResponse;
import com.example.order_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceWrapper {

    private final ProductClient productClient;

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    public ApiResponse<ProductResponse> getProduct(Long productId) {
        return productClient.getProductById(productId);
    }

    public ApiResponse<ProductResponse> getProductFallback(Long productId, Throwable ex) {
        throw new ServiceUnavailableException("Product service is unavailable (fallback triggered)");
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "reduceStockFallback")
    public void reduceStock(Long productId, Integer qty) {
        productClient.reduceStock(productId, qty);
    }

    public void reduceStockFallback(Long productId, Integer qty, Throwable ex) {
        throw new ServiceUnavailableException("Product service unavailable while reducing stock");
    }

    public void increaseStock(Long productId, Integer qty) {
        productClient.increaseStock(productId, qty);
    }
}
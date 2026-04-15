package com.example.cart_service.external;

import com.example.cart_service.client.ProductClient;
import com.example.cart_service.dto.ApiResponse;
import com.example.cart_service.dto.ProductResponse;
import com.example.cart_service.exception.ServiceUnavailableException;
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
        throw new ServiceUnavailableException(
                "Product service is temporarily unavailable. Please try again later."
        );
    }
}

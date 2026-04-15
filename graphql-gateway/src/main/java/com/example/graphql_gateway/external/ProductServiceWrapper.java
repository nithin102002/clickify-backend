package com.example.graphql_gateway.external;


import com.example.graphql_gateway.client.ProductClient;
import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.ProductResponse;
import com.example.graphql_gateway.exception.ServiceUnavailableException;
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


}
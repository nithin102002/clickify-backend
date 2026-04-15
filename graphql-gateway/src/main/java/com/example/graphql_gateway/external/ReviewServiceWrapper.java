package com.example.graphql_gateway.external;

import com.example.graphql_gateway.client.ReviewClient;
import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.ReviewResponse;
import com.example.graphql_gateway.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceWrapper {

    private final ReviewClient reviewClient;

    @CircuitBreaker(name = "reviewService", fallbackMethod = "reviewFallback")
    public ApiResponse<List<ReviewResponse>> getReviews(Long productId) {
        return reviewClient.getReviews(productId);
    }

    public ApiResponse<List<ReviewResponse>> reviewFallback(Long productId, Throwable ex) {

        return new ApiResponse<>(
                "SUCCESS",
                "Fallback: reviews unavailable",
                List.of()
        );
    }
}

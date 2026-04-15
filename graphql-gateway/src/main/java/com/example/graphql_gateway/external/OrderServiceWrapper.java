package com.example.graphql_gateway.external;

import com.example.graphql_gateway.client.OrderClient;
import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.OrderResponse;
import com.example.graphql_gateway.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceWrapper {

    private final OrderClient orderClient;

    @CircuitBreaker(name = "orderService", fallbackMethod = "orderFallback")
    public ApiResponse<OrderResponse> getOrder(Long id) {
        return orderClient.getOrder(id);
    }

    public ApiResponse<OrderResponse> orderFallback(Long id, Throwable ex) {
        throw new ServiceUnavailableException("Order service unavailable");
    }
}
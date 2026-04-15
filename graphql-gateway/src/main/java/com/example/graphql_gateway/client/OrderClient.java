package com.example.graphql_gateway.client;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/v1/orders/{id}")
    ApiResponse<OrderResponse> getOrder(@PathVariable Long id);
}

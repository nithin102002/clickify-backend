package com.example.graphql_gateway.client;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable Long id);
}

package com.example.order_service.client;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {


    @GetMapping("/api/v1/products/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable Long id);

    @PutMapping("/api/v1/products/{id}/reduce-stock")
    void reduceStock(@PathVariable Long id, @RequestParam int quantity);

    @PutMapping("/api/v1/products/{id}/increase-stock")
    void increaseStock(@PathVariable Long id, @RequestParam int quantity);




}

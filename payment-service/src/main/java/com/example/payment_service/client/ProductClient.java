package com.example.payment_service.client;



import com.example.payment_service.dto.ApiResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PutMapping("/api/v1/products/{id}/increase-stock")
    void increaseStock(@PathVariable Long id, @RequestParam int quantity);
}

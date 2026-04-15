package com.example.graphql_gateway.client;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "review-service")
public interface ReviewClient {

    @GetMapping("/api/v1/reviews/product/{productId}")
    ApiResponse<List<ReviewResponse>> getReviews(@PathVariable Long productId);
}

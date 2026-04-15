package com.example.review_service.api;

import com.example.review_service.constants.ApiConstants;
import com.example.review_service.dto.ApiResponse;
import com.example.review_service.dto.ReviewRequest;
import com.example.review_service.dto.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ApiConstants.BASIC_API_URL)
public interface ReviewApi {

    @PostMapping
    ResponseEntity<ApiResponse<ReviewResponse>> addReview(
            @RequestHeader("X-User") String username,
            @RequestBody ReviewRequest request
    );

    @GetMapping(ApiConstants.REVIEWS_BY_ID)
    ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(
            @PathVariable Long productId
    );
}

package com.example.review_service.controller;

import com.example.review_service.api.ReviewApi;
import com.example.review_service.constants.MessageConstants;
import com.example.review_service.dto.ApiResponse;
import com.example.review_service.dto.ReviewRequest;
import com.example.review_service.dto.ReviewResponse;
import com.example.review_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {

    private final ReviewService reviewService;

    @Override
    public ResponseEntity<ApiResponse<ReviewResponse>> addReview(
            String username,
            ReviewRequest request) {

        ReviewResponse response = reviewService.addReview(username, request);

        return ResponseEntity.ok(
                ApiResponse.<ReviewResponse>builder()
                        .status("SUCCESS")
                        .message(MessageConstants.REVIEW_ADDED)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(Long productId) {

        List<ReviewResponse> responses =
                reviewService.getReviewsByProduct(productId);

        return ResponseEntity.ok(
                ApiResponse.<List<ReviewResponse>>builder()
                        .status("SUCCESS")
                        .message(MessageConstants.REVIEW_FETCHED)
                        .data(responses)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}

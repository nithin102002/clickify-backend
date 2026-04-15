package com.example.review_service.service;

import com.example.review_service.dto.ReviewRequest;
import com.example.review_service.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(String username, ReviewRequest request);

    List<ReviewResponse> getReviewsByProduct(Long productId);
}

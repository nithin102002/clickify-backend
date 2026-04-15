package com.example.review_service.service.impl;

import com.example.review_service.dto.ReviewRequest;
import com.example.review_service.dto.ReviewResponse;
import com.example.review_service.entity.Review;
import com.example.review_service.exception.BadRequestException;
import com.example.review_service.mapper.ReviewMapper;
import com.example.review_service.repository.ReviewRepoService;
import com.example.review_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepoService reviewRepoService;
    private final ReviewMapper reviewMapper;
    @Override
    public ReviewResponse addReview(String username, ReviewRequest request) {
        // Rating validation
        if (request.rating() < 1 || request.rating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        // Duplicate check
        Optional<Review> existingReview =
                reviewRepoService.findByProductIdAndUsername(
                        request.productId(),
                        username
                );

        if (existingReview.isPresent()) {
            throw new BadRequestException("You have already reviewed this product");
        }

        Review review = Review.builder()
                .productId(request.productId())
                .username(username)
                .rating(request.rating())
                .comment(request.comment())
                .createdAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepoService.save(review);

        return reviewMapper.toResponse(saved);
    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        List<Review> reviews = reviewRepoService.findByProductId(productId);

        return reviewMapper.toResponseList(reviews);
    }
}

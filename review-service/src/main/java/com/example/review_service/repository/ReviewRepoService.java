package com.example.review_service.repository;

import com.example.review_service.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepoService {
    Review save(Review review);

    List<Review> findByProductId(Long productId);

    Optional<Review> findByProductIdAndUsername(Long productId, String username);
}

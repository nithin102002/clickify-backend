package com.example.review_service.repository.impl;

import com.example.review_service.entity.Review;
import com.example.review_service.repository.ReviewRepoService;
import com.example.review_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewRepoServiceImpl implements ReviewRepoService {

    private final ReviewRepository reviewRepository;
    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Optional<Review> findByProductIdAndUsername(Long productId, String username) {
        return reviewRepository.findByProductIdAndUsername(productId, username);
    }
}

package com.example.review_service.repository;

import com.example.review_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByProductId(Long productId);

    Optional<Review> findByProductIdAndUsername(Long productId, String username);
}

package com.example.review_service.mapper;

import com.example.review_service.dto.ReviewResponse;
import com.example.review_service.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewResponse toResponse(Review review);

    List<ReviewResponse> toResponseList(List<Review> reviews);
}
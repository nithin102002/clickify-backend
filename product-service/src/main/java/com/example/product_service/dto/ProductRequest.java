package com.example.product_service.dto;

import com.example.product_service.enums.ProductStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product Name is Required")
        String name,
        @Size(max=2000, message = "Description is too long")
        String description,
        @NotNull(message = "Price is Required")
        @DecimalMin(value = "0.0",inclusive = false,message = "Price must be Positive")
        BigDecimal price,
        String category,
        @NotNull(message = "Stock quantity is required")
        @Min(value=0,message = "Stock cannot be negative ")
        Integer stockQuantity,
        @NotNull(message="Status is Required")
        ProductStatus productStatus

) {
}

package com.example.product_service.repository;

import com.example.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface ProductRepoService {
    Product createProduct(Product product);
    Page<Product> getAllProduct(Pageable pageable);

    Optional<Product> getProductById(Long id);


    Product updateProduct(Product existingProduct);

    void deleteById(Long id);


    Page<Product> searchProduct(Specification<Product> spec, Pageable pageable);
}

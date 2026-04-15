package com.example.product_service.repository.Impl;

import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepoService;
import com.example.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductRepoServiceImpl implements ProductRepoService {

    private final ProductRepository productRepository;
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Page<Product> getAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Product existingProduct) {
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> searchProduct(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec,pageable);
    }
}

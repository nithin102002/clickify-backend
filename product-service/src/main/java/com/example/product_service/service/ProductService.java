package com.example.product_service.service;

import com.example.product_service.dto.PageResponse;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest );

    PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir);
    
    ProductResponse getProductById(Long id);
    
    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);


    PageResponse<ProductResponse> searchProducts(String name, Double minPrice, Double maxPrice, Boolean inStock, int page, int size);

    ProductResponse uploadProductImage(Long id, MultipartFile file);

    void reduceStock(Long id, int quantity);

    void increaseStock(Long id, int quantity);
}

package com.example.product_service.controller;

import com.example.product_service.api.ProductApi;
import com.example.product_service.constants.MessageConstants;
import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.PageResponse;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.enums.ApiStatus;
import com.example.product_service.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private  final ProductService productService;

    @Override
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(ProductRequest productRequest) {
       ProductResponse productResponse=productService.createProduct(productRequest);

       return ResponseEntity.ok(
               ApiResponse.<ProductResponse>builder()
                       .status(ApiStatus.SUCCESS)
                       .message(MessageConstants.PRODUCT_CREATED)
                       .data(productResponse)
                       .timestamp(LocalDateTime.now())
                       .build()
       );
    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(int page, int size, String sortBy, String sortDir) {
        PageResponse<ProductResponse> response=productService.getAllProducts(page,size,sortBy,sortDir);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ProductResponse>>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PRODUCT_FOUND)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

    }

    @Override
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(Long id) {
        ProductResponse response=productService.getProductById(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PRODUCT_FOUND)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

    }

    @Override
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(Long id, ProductRequest productRequest) {
        ProductResponse response=productService.updateProduct(id,productRequest);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PRODUCT_UPDATE)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteProductById(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PRODUCT_DELETED)
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(String name, Double minPrice, Double maxPrice, Boolean inStock, int page, int size) {
        PageResponse<ProductResponse> response=productService.searchProducts(name,minPrice,maxPrice,inStock,page,size);
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ProductResponse>>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PRODUCT_FOUND)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<ProductResponse>> uploadProductImage(Long id, MultipartFile file) {
       ProductResponse response=productService.uploadProductImage(id,file);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.IMAGE_UPLOAD)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );

    }

    @Override
    public ResponseEntity<ApiResponse<Void>> reduceStock(Long id, int quantity) {

        productService.reduceStock(id, quantity);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .message("Stock reduced successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> increaseStock(Long id, int quantity) {
        productService.increaseStock(id, quantity);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .message("Stock Increased successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}

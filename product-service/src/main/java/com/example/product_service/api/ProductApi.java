package com.example.product_service.api;

import com.example.product_service.constants.ApiConstants;
import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.PageResponse;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(ApiConstants.BASIC_API_URL)
public interface ProductApi {

    @PostMapping(ApiConstants.PRODUCTS)
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid  @RequestBody ProductRequest productRequest);

    @GetMapping(ApiConstants.PRODUCTS)
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir);

    @GetMapping(ApiConstants.PRODUCTS_BY_ID)
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id);


    @PutMapping(ApiConstants.PRODUCTS_BY_ID)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @Valid  @RequestBody ProductRequest productRequest);

    @DeleteMapping(ApiConstants.PRODUCTS_BY_ID)
    public ResponseEntity<ApiResponse<Void>> deleteProductById(@PathVariable Long id);

    @GetMapping(ApiConstants.SEARCH_PRODUCT)
    ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @PostMapping(
            value = ApiConstants.UPLOAD_PRODUCT_IMAGE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<ApiResponse<ProductResponse>> uploadProductImage(
            @PathVariable Long id,
            @RequestParam("file")MultipartFile file);

    @PutMapping(ApiConstants.REDUCE_STOCK)
    ResponseEntity<ApiResponse<Void>> reduceStock(
            @PathVariable Long id,
            @RequestParam int quantity
    );

    @PutMapping(ApiConstants.INCREASE_STOCK)
    ResponseEntity<ApiResponse<Void>> increaseStock(@PathVariable Long id, @RequestParam int quantity);

}

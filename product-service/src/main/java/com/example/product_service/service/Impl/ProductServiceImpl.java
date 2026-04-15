package com.example.product_service.service.Impl;

import com.example.product_service.constants.MessageConstants;
import com.example.product_service.dto.PageResponse;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.entity.Product;
import com.example.product_service.enums.ProductStatus;
import com.example.product_service.exception.BadRequestException;
import com.example.product_service.exception.ResourceNotFoundException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.ProductRepoService;
import com.example.product_service.service.ProductService;
import com.example.product_service.service.storage.FileStorageService;
import com.example.product_service.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepoService productRepoService;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;


    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest productRequest) {

        log.info("Creating product: {}", productRequest.name());

        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepoService.createProduct(product);

        return productMapper.toResponse(savedProduct);
    }


    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {


        log.info("DB HIT → getProductById called for ID: {}", id);

        Product product = productRepoService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.PRODUCT_NOT_FOUND + id));

        return productMapper.toResponse(product);
    }


    @Override
    @Cacheable(
            value = "products",
            key = "#page + '-' + #size + '-' + #sortBy + '-' + #sortDir"
    )
    public PageResponse<ProductResponse> getAllProducts(
            int page, int size, String sortBy, String sortDir) {


        log.info("DB HIT  getAllProducts called | page={}, size={}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepoService.getAllProduct(pageable);

        Page<ProductResponse> responsePage =
                productPage.map(productMapper::toResponse);

        return new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        );
    }


    @Override
    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        log.info("Updating product ID: {}", id);

        Product existingProduct = productRepoService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.PRODUCT_NOT_FOUND + id));

        existingProduct.setName(request.name());
        existingProduct.setDescription(request.description());
        existingProduct.setPrice(request.price());
        existingProduct.setCategory(request.category());
        existingProduct.setStockQuantity(request.stockQuantity());
        existingProduct.setProductStatus(request.productStatus());

        Product updated = productRepoService.updateProduct(existingProduct);

        return productMapper.toResponse(updated);
    }


    @Override
    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {

        log.info("Deleting product ID: {}", id);

        Product product = productRepoService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.PRODUCT_NOT_FOUND + id));

        fileStorageService.deleteFile(product.getImageUrl());
        productRepoService.deleteById(id);
    }


    @Override
    public PageResponse<ProductResponse> searchProducts(
            String name, Double minPrice, Double maxPrice,
            Boolean inStock, int page, int size) {

        log.info("Searching products");

        Pageable pageable = PageRequest.of(page, size);

        Specification<Product> spec = Specification.allOf(
                ProductSpecification.nameContains(name),
                ProductSpecification.priceGreaterThan(minPrice),
                ProductSpecification.priceLessThan(maxPrice),
                ProductSpecification.inStock(inStock)
        );

        Page<Product> pageResult =
                productRepoService.searchProduct(spec, pageable);

        List<ProductResponse> content = pageResult.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }


    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse uploadProductImage(Long id, MultipartFile file) {

        log.info("Uploading image for product ID: {}", id);

        Product product = productRepoService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.PRODUCT_NOT_FOUND + id));

        if (product.getImageUrl() != null) {
            fileStorageService.deleteFile(product.getImageUrl());
        }

        String imageUrl = fileStorageService.storeFile(file);

        product.setImageUrl(imageUrl);

        Product saved = productRepoService.updateProduct(product);

        return productMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public void reduceStock(Long id, int quantity) {

        log.info("Reducing stock for product ID: {} by {}", id, quantity);

        Product product = productRepoService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.PRODUCT_NOT_FOUND + id));

        if (product.getProductStatus() == ProductStatus.INACTIVE) {
            throw new BadRequestException("Product is inactive");
        }

        if (product.getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);

        productRepoService.updateProduct(product);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public void increaseStock(Long id, int quantity) {
        Product product = productRepoService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.PRODUCT_NOT_FOUND + id));

        product.setStockQuantity(product.getStockQuantity() + quantity);

        productRepoService.updateProduct(product);
    }
}
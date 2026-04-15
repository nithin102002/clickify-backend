package com.example.product_service.constants;

public class ApiConstants {
    private ApiConstants(){}


    public static final String BASIC_API_URL="/api/v1";
    public static final String PRODUCTS="/products";
    public static final String PRODUCTS_BY_ID="/products/{id}";
    public static final String REDUCE_STOCK="/products/{id}/reduce-stock";
    public static final String INCREASE_STOCK="/products/{id}/increase-stock";
    public static final String SEARCH_PRODUCT="/products/search";
    public static final String UPLOAD_PRODUCT_IMAGE="/products/{id}/image";
}

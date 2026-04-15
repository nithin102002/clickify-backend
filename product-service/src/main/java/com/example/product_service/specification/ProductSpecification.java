package com.example.product_service.specification;

import com.example.product_service.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<Product> priceGreaterThan(Double minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null :
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessThan(Double maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null :
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> inStock(Boolean inStock) {
        return (root, query, cb) ->
                inStock == null ? null :
                        inStock
                                ? cb.greaterThan(root.get("stockQuantity"), 0)
                                : cb.equal(root.get("stockQuantity"), 0);
    }
}

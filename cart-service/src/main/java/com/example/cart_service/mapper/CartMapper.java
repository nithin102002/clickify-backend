package com.example.cart_service.mapper;

import com.example.cart_service.dto.CartItemResponse;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponse toResponse(Cart cart);

    CartItemResponse toItemResponse(CartItem item);

    List<CartItemResponse> toItemResponseList(List<CartItem> items);
}
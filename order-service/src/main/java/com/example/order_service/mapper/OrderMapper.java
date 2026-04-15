package com.example.order_service.mapper;

import com.example.order_service.dto.OrderItemResponse;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "status", target = "status")
    OrderResponse toResponse(Order order);

    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toItemResponseList(List<OrderItem> items);

    //  ENUM → STRING
    default String mapStatus(Enum<?> status) {
        return status != null ? status.name() : null;
    }
}

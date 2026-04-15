package com.example.order_service.api;

import com.example.order_service.constants.ApiConstants;
import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ApiConstants.BASIC_API_URL)
public interface OrderApi {

    @PostMapping(ApiConstants.ORDERS)
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestHeader("X-User") String username, @RequestBody OrderRequest request);

    @GetMapping(ApiConstants.ORDERS)
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getUserOrders(
            @RequestHeader("X-User") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long orderId
    );

    @GetMapping(ApiConstants.ORDERS + "/{id}")
    ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long id
    );

    @PutMapping(ApiConstants.UPDATE_ORDER_STATUS)
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    );
}

package com.example.userservice.api;

import com.example.userservice.constants.ApiConstants;
import com.example.userservice.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ApiConstants.BASIC_API_URL)
public interface AuthApi {

    @PostMapping(ApiConstants.SIGN_UP)
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request);

    @PostMapping(ApiConstants.LOG_IN)
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request);

    @PostMapping(ApiConstants.REFRESH)
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshRequest refreshRequest);

    @PostMapping(ApiConstants.LOG_OUT)
    public ApiResponse<Void> logout(@Valid @RequestBody RefreshRequest refreshRequest);



}

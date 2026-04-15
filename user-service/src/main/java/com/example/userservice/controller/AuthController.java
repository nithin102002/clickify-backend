package com.example.userservice.controller;

import com.example.userservice.api.AuthApi;
import com.example.userservice.dto.*;
import com.example.userservice.enums.ApiStatus;
import com.example.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    @Override
    public ApiResponse<UserResponse> register(RegisterRequest request) {
       UserResponse userResponse= authService.register(request);

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                "User registered successfully",
                userResponse,
                LocalDateTime.now()
        );
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        AuthResponse response = authService.login(request);

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                "Login successful",
                response,
                LocalDateTime.now()
        );
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(RefreshRequest refreshRequest) {

        AuthResponse response = authService.refreshToken(refreshRequest);
        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                "Refresh Token",
                response,
                LocalDateTime.now()
        );
    }

    @Override
    public ApiResponse<Void> logout(RefreshRequest refreshRequest) {
        authService.logout(refreshRequest.refreshToken());
        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                "Logout successful",
                null,
                LocalDateTime.now()
        );
    }
}

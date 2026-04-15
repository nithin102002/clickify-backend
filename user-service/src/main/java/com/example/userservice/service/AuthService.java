package com.example.userservice.service;

import com.example.userservice.dto.*;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {

    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshRequest refreshRequest);


    void logout(String refreshToken);
}

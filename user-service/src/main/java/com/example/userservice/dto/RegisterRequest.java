package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        String firstName,
        String middleName,
        @NotBlank
        String lastName,
        @NotBlank
        String username,
        @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
        String phoneNumber,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}

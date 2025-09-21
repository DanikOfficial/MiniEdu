package com.miniedu.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Email is required and cannot be blank")
                           @Email(message = "Please provide a valid email address") String email,
                           @NotBlank(message = "Password is required and cannot be blank")
                           String password) {
}

package com.miniedu.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotBlank(message = "Username is mandatory!") String username,
                                  @NotBlank(message = "Email is required and cannot be blank")
                              @Email(message = "Please provide a valid email address") String email,
                                  @NotBlank(message = "Password is required and cannot be blank")
                              @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
}

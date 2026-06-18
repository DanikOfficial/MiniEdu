package com.miniedu.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(@NotBlank(message = "Email is required and cannot be blank")
                                    @Email(message = "Please provide a valid email address") String email) {
}

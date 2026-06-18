package com.miniedu.userservice.dto.response;

public record AuthResponse(String token, UserResponse userResponse) {
}

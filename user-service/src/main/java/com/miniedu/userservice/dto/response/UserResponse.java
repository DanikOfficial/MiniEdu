package com.miniedu.userservice.dto.response;

import com.miniedu.userservice.entity.User;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email, String role) {

    public static UserResponse map(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().toString());
    }
}

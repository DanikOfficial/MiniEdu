package com.miniedu.userservice.service;

import com.miniedu.userservice.dto.request.PasswordChangeRequest;
import com.miniedu.userservice.dto.response.AuthResponse;
import com.miniedu.userservice.dto.request.LoginRequest;
import com.miniedu.userservice.dto.request.RegisterUserRequest;
import com.miniedu.userservice.dto.response.MessageResponse;

public interface UserService {

    MessageResponse register(RegisterUserRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse activateUser(String token);

    MessageResponse requestUserPasswordChange(PasswordChangeRequest request);

    AuthResponse reactivateUser(String email);
}

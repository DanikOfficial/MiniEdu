package com.miniedu.userservice.controller;

import com.miniedu.userservice.dto.request.PasswordChangeRequest;
import com.miniedu.userservice.dto.request.RegisterUserRequest;
import com.miniedu.userservice.dto.response.MessageResponse;
import com.miniedu.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        MessageResponse res = userService.register(request);

        return ResponseEntity.ok(res);
    }

    @PostMapping(value = "/users/password/request/change", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MessageResponse> requestUserPasswordReset(@RequestBody @Valid PasswordChangeRequest request) {
        MessageResponse res = userService.requestUserPasswordChange(request);

        return ResponseEntity.ok(res);
    }
}

package com.dostiship.controller;

import com.dostiship.dto.AuthResponse;
import com.dostiship.dto.LoginRequest;
import com.dostiship.dto.UserRegistrationRequest;
import com.dostiship.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account and return JWT token")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegistrationRequest signUpRequest) {
        AuthResponse response = userService.registerUser(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password and return JWT token")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}
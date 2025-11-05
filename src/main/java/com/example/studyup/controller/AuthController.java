package com.example.studyup.controller;

// ✅ The entity class for users
import com.example.studyup.model.AppUser;

// ✅ DTO (Data Transfer Object) for registration data
import com.example.studyup.dto.UserRegistrationDto;

// ✅ Business logic layer
import com.example.studyup.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ✅ REST Controller for authentication actions (register, login).
 * Connected to frontend routes starting with /api/auth/*
 */
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;

    /**
     * ✅ Constructor-based dependency injection.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ✅ Register a new user.
     *
     * Example JSON:
     * {
     *   "username": "nurzada",
     *   "email": "nurzada@example.com",
     *   "password": "MySecurePass123"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserRegistrationDto dto) {
        try {
            // ✅ Use the new entity type AppUser
            AppUser newUser = userService.registerUser(dto);

            // ✅ Return the created user (without password)
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}

package com.example.studyup.controller;
// 👆 This file lives in src/main/java/com/example/studyUp/controller/

import com.example.studyup.dto.UserRegistrationDto;
// ✅ DTO (Data Transfer Object) that carries data from the frontend

import com.example.studyup.entity.User;
// ✅ The User entity that represents a user record in the database

import com.example.studyup.service.UserService;
// ✅ Our service layer interface that handles business logic

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ✅ REST Controller for authentication-related actions (register, login).
 * This will be connected to your React frontend at /api/auth/*
 */
@RestController // Marks this as a REST controller that returns JSON instead of HTML
@RequestMapping("/api/auth") // Base URL path for all authentication APIs
@Validated // Enables @Valid validation for DTOs
public class AuthController {

    private final UserService userService; // Dependency: service to handle logic

    /**
     * ✅ Constructor-based injection.
     * Spring automatically provides the correct implementation (UserServiceImpl).
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ✅ POST endpoint: Register a new user
     * Example request (from React or Postman):
     * {
     *   "username": "new user",
     *   "email": "newuser@example.com",
     *   "password": "StrongPass123"
     * }
     *
     * @param dto JSON request body mapped into UserRegistrationDto
     * @return Created User (without exposing password)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserRegistrationDto dto) {
        try {
            // ✅ Calls the service to handle registration logic
            User newUser = userService.registerUser(dto);

            // ✅ Return success (201 Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        } catch (Exception e) {
            // ✅ If something goes wrong (e.g., username already exists)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}

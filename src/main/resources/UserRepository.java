package com.example.studyup.dto; // ✅ match the rest of your project

import lombok.Data;

/**
 * 🧩 UserRegistrationDto
 *
 * DTO for user registration requests.
 * Carries the user's username and password from the frontend (React)
 * to the backend (Spring Boot).
 */
@Data
public class UserRegistrationDto {

    /**
     * 👤 Username entered by the user during registration.
     * Must be unique (enforced by the User entity).
     */
    private String username;

    /**
     * 🔐 Plain text password sent from the frontend.
     * Will be hashed by PasswordEncoder before saving in the database.
     */
    private String password;
}

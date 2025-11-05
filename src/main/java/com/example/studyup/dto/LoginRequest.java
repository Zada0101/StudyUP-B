package com.example.studyup.dto; // ✅ Lowercase package path — must match folder structure exactly

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ✅ LoginRequest DTO
 * Carries username and password from frontend → backend during login.
 */
@Getter
@Setter
public class LoginRequest {

    // ✅ Getters and setters — required for JSON <-> Java mapping
    @NotBlank(message = "Username is required")
    private String username; // 🧍 User’s username

    @NotBlank(message = "Password is required")
    private String password; // 🔐 User’s password

}

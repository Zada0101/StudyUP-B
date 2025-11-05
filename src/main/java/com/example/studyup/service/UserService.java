package com.example.studyup.service;
// 👆 This defines the package (folder path) where this file lives.
// Path: src/main/java/com/example/studyUp/service/

import com.example.studyup.dto.UserRegistrationDto;
// ✅ Data Transfer Object (DTO) that holds the registration form data from frontend

import com.example.studyup.entity.User;
// ✅ Import the User entity (the actual DB-mapped class)

/**
 * ✅ Interface that defines all user-related business operations.
 *
 * Why an interface?
 * - It separates the *definition* of logic from its *implementation*.
 * - Makes the app easier to test (you can mock it in tests).
 * - Allows multiple implementations later (e.g., DB, external API, etc.)
 */
public interface UserService {

    /**
     * ✅ Defines a method for registering a new user.
     *
     * @param dto  User registration details (username, email, password)
     * @return     The newly registered User (after saving to DB)
     */
    User registerUser(UserRegistrationDto dto);
}

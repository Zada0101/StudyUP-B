package com.example.studyup.service; // Service layer for authentication logic

import com.example.studyup.dto.UserRegistrationDto; // Data from the registration form

/**
 * ✅ Authentication Service Interface
 * Defines what authentication-related features must exist in the system.
 * Implementation (AuthServiceImpl) will provide the actual logic.
 */
public interface AuthService {

    /**
     * ✅ Register a new user.
     * @param dto - The user’s registration details (username, password, etc.)
     *              Comes from the frontend.
     */
    void register(UserRegistrationDto dto);

    /**
     * ✅ Login a user.
     * @param username - The username provided during login.
     * @param password - The raw password (to be validated against stored hash).
     * @return A token (e.g., JWT or temporary dev token) if credentials are valid.
     */
    String login(String username, String password);
}

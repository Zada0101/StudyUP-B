// This package shows that this interface belongs to the SERVICE layer
//  contains business logic
package com.example.studyup.service;

// DTO used to receive user registration data from the controller
import com.example.studyup.dto.UserRegistrationDto;

// AppUser is the user entity saved in the database
import com.example.studyup.model.AppUser;

// AuthService is an INTERFACE
// It defines what authentication-related actions are available
public interface AuthService {

    // This method is used to REGISTER a new user
    // Input:
    // UserRegistrationDto → contains username, email, password (plain text)
    // What implementation should do:
    // 1. Validate input
    // 2. Hash the password
    // 3. Create AppUser entity
    // 4. Save user to database
    //
    // Output:
    // AppUser → the saved user with hashed password
    AppUser register(UserRegistrationDto dto);

    // This method is used to LOG IN a user
    //
    // Input:
    // username → entered by user
    // rawPassword → plain text password entered by user
    //
    // What implementation should do:
    // 1. Find user by username
    // 2. Compare raw password with stored hashed password
    // 3. If correct → return token or success value
    //
    // Output:
    // String → usually a token (JWT) or success message
    String login(String username, String rawPassword);
}

package com.example.studyup.service; // Folder: com/example/studyUp/service

import com.example.studyup.dto.UserRegistrationDto;          // Registration data (username, password)
import com.example.studyup.entity.User;                      // Database entity
import com.example.studyup.repository.UserRepository;        // For DB operations
import org.springframework.security.crypto.password.PasswordEncoder; // To hash passwords
import org.springframework.stereotype.Service;               // Marks this as a Spring Service
import org.springframework.transaction.annotation.Transactional; // Ensures DB consistency

/**
 * ✅ This class implements authentication logic.
 * Handles user registration and login validation.
 */
@Service
public class AuthServiceImpl implements AuthService {

    // Inject dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * ✅ Constructor-based dependency injection.
     * Spring automatically provides these beans.
     */
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ Register a new user
     * Steps:
     * 1️⃣ Check if username already exists
     * 2️⃣ Encode (hash) the password
     * 3️⃣ Save the new user to the database
     */
    @Override
    @Transactional // Ensures all DB operations in this method complete successfully or roll back
    public void register(UserRegistrationDto dto) {

        // Step 1: Prevent duplicate usernames
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }

        // Step 2: Create a new User entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword())); // Securely hash password
        user.setEmail(dto.getEmail()); // Optional: add email if available

        // Step 3: Save user to database
        userRepository.save(user);
    }

    /**
     * ✅ Login user
     * Steps:
     * 1️⃣ Look up user by username
     * 2️⃣ Compare raw password to stored hash
     * 3️⃣ Return a token if valid (simple placeholder now)
     */
    @Override
    public String login(String username, String password) {
        // Step 1: Look up user by username
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Step 2: Get user object
        var user = userOpt.get();

        // Step 3: Compare passwords using encoder
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Step 4: Return a simple token (later replaced by JWT)
        return "dev-token-" + username; // Just a fake token for now
    }
}

package com.example.studyup.service;
// 👆 Must match your folder path exactly: src/main/java/com/example/studyUp/service

import com.example.studyup.dto.UserRegistrationDto; // DTO from frontend
import com.example.studyup.entity.User;             // User entity (database)
import com.example.studyup.repository.UserRepository; // Handles DB operations
import com.example.studyup.exception.UserAlreadyExistsException; // Custom exception
import org.springframework.security.crypto.password.PasswordEncoder; // For hashing passwords
import org.springframework.stereotype.Service; // Marks this as a service component
import org.springframework.transaction.annotation.Transactional; // Makes DB writes safe

/**
 * ✅ Handles all user-related business logic (register, lookup, etc.)
 */
@Service
public class UserServiceImpl implements UserService {

    // ✅ Dependencies injected by Spring (no need to 'new' them yourself)
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Constructor injection — best practice in Spring
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ Registers a new user safely.
     * Steps:
     * 1️⃣ Check for duplicate username/email
     * 2️⃣ Hash password with BCrypt
     * 3️⃣ Save new User to database
     * 4️⃣ Return the saved User
     */
    @Override
    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        // 1️⃣ Prevent duplicate usernames
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + dto.getUsername());
        }

        // (Optional) Prevent duplicate emails
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + dto.getEmail());
        }

        // 2️⃣ Hash password before saving
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3️⃣ Create User entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(encodedPassword);

        // 4️⃣ Save user to DB
        return userRepository.save(user);
    }
}

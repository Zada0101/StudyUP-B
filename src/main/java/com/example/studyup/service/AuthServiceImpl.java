package com.example.studyup.service;

import com.example.studyup.dto.UserRegistrationDto;
import com.example.studyup.model.AppUser;
import com.example.studyup.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ✅ Handles authentication logic (register + login)
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ✅ Registers a new user safely.
     */
    @Override
    @Transactional
    public void register(UserRegistrationDto dto) {
        // 1️⃣ Check for existing username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }

        // 2️⃣ Optional: check for duplicate email
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // 3️⃣ Create new AppUser and encode password
        AppUser user = AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .build();

        // 4️⃣ Save to DB
        userRepository.save(user);
    }

    /**
     * ✅ Logs in user and returns a temporary dev token.
     */
    @Override
    public String login(String username, String password) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        AppUser user = userOpt.get();

        // Compare raw password to encoded hash
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Return a simple dev token (replace later with JWT)
        return "dev-token-" + username;
    }
}

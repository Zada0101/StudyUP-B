package com.example.studyup.service;

import com.example.studyup.dto.UserRegistrationDto;
import com.example.studyup.model.AppUser;
import com.example.studyup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser register(UserRegistrationDto dto) {

        // Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create user with passwordHash
        AppUser user = AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword())) // FIXED
                .role("USER")
                .build();

        return userRepository.save(user); // Return full object
    }

    @Override
    public String login(String username, String password) {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        // Compare password with hashed passwordHash
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {  // FIXED
            throw new IllegalArgumentException("Invalid password");
        }

        // Temporary token (replace later with JWT)
        return "TOKEN_" + user.getId();
    }
}

package com.example.studyup.service;

import com.example.studyup.dto.UserRegistrationDto;
import com.example.studyup.model.AppUser;
import com.example.studyup.repository.UserRepository;
import com.example.studyup.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AppUser registerUser(UserRegistrationDto dto) {

        // Check username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username already exists: " + dto.getUsername()
            );
        }

        // Check email
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Email already exists: " + dto.getEmail()
            );
        }

        // Build user
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        // 🔥 Always encode password
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // Default role
        user.setRole("USER");

        return userRepository.save(user);
    }
}

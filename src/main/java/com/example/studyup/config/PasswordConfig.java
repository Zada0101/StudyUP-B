package com.example.studyup.config; // Config package


import org.springframework.context.annotation.Bean; // Define beans
import org.springframework.context.annotation.Configuration; // Config class
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCrypt encoder
import org.springframework.security.crypto.password.PasswordEncoder; // Interface


@Configuration // Password configuration
public class PasswordConfig {
    @Bean // Expose a PasswordEncoder bean to encode/verify passwords
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strong hashing algorithm
    }
}
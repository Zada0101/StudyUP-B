package com.study.StudyUp.controller;

import com.study.StudyUp.DTOs.UserLoginDto;
import com.study.StudyUp.DTOs.UserRegistrationDto;
import com.study.StudyUp.model.User;
import com.study.StudyUp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//This class handles web requests and sends back data - Handles login/register
@RestController
//Sets the main URL for the class - /api/auth
@RequestMapping("/api/auth")

//data that handles login and registration.
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;



    //The constructor gives the controller access to UserService and PasswordEncoder,
    // so it can save users and check passwords.
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //@PostMapping->tells to Spring Boot->Run this method when someone sends
    // a POST request-sending data to create something new (like registering or login a user).
    //ResponseEntity<?> → We’re sending a response back to the client with a message and an HTTP status code.
    //@Valid → Checks if the user filled the required fields correctly (like username, password, email).
    //@RequestBody->Reads the JSON data sent by the user and converts it into a Java object (dto)
//DTO(Data Transfer Object)-only contains the fields(data) that the user sends or receives
    //BindingResult result → stores any errors found during this validation.

//passwordEncoder.matches(...) → Compares the typed password with the stored hashed password.
    //If it’s wrong → send 401 Unauthorized.
    //If it’s correct → send 200 OK and success message.
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto) {
        User user = userService.findByUsername(loginDto.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }


        boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash());
        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        return ResponseEntity.ok("Login successful");
    }
}

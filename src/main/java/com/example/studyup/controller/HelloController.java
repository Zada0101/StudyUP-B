// Package name shows this class belongs to controller layer
package com.example.studyup.controller;

// Import GetMapping annotation for handling GET requests
import org.springframework.web.bind.annotation.GetMapping;

// Import RestController annotation
import org.springframework.web.bind.annotation.RestController;

/**
 * ✅ Simple controller to confirm the server is working.
 * This controller is used just to test if backend is up.
 */

// @RestController tells Spring Boot:
// 1) This class will receive HTTP requests
// 2) This class will return responses directly (String / JSON)
@RestController
public class HelloController {

    // @GetMapping("/") means:
    // When someone opens http://localhost:8080/
    // this method will run
    @GetMapping("/")
    public String home() {

        // This String is sent back as HTTP response
        // Used to confirm backend is running
        return "🚀 StudyUp backend is running successfully!";
    }

    // @GetMapping("/api/health") means:
    // When frontend or browser calls http://localhost:8080/api/health
    // this method will run
    @GetMapping("/api/health")
    public String healthCheck() {

        // Simple health-check response
        // Used to test if API is alive
        return "✅ API is healthy!";
    }
}

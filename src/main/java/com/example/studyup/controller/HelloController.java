package com.example.studyup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ✅ Simple controller to confirm the server is working.
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "🚀 StudyUp backend is running successfully!";
    }

    @GetMapping("/api/health")
    public String healthCheck() {
        return "✅ API is healthy!";
    }
}

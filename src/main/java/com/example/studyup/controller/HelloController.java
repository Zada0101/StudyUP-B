package com.example.studyup.controller; // Controllers package


import org.springframework.web.bind.annotation.GetMapping; // Map GET
import org.springframework.web.bind.annotation.RestController; // REST controller


@RestController // Marks this class as a REST controller
public class HelloController {
    @GetMapping("/hello") // Basic test endpoint: GET /hello
    public String hello() { // Returns plain text
        return "Hello, StudyUp!"; // Useful for Week 1 verification
    }
}
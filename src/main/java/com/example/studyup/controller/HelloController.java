package com.example.studyup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ✅ Simple controller to confirm the server is working.
 */

//This annotation tells to Spring boot
// this class should receive HTTP requests and send responses
@RestController
public class HelloController {


    //@GetMapping->is an annotation that tells Spring when to run a method; the method contains the code that runs
    //home()->Method	Runs code and returns response
    @GetMapping("/")
    public String home() {
        return "🚀 StudyUp backend is running successfully!";
    }

    @GetMapping("/api/health")
    public String healthCheck() {
        return "✅ API is healthy!";
    }
}

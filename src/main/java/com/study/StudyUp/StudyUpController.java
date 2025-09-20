package com.study.StudyUp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class StudyUpController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello and Welcome to StudyUp!";
    }
}


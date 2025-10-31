package com.study.StudyUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.study.StudyUp")
public class StudyUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyUpApplication.class, args);
        System.out.println("✅ StudyUp server is running...");
    }
}

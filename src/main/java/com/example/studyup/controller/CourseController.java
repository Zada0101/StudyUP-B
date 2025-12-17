// This package shows that this class is a CONTROLLER
// Controllers handle HTTP requests from frontend
package com.example.studyup.controller;

// CourseDto is used to send course data to frontend
import com.example.studyup.dto.CourseDto;

// SectionDto is used to send section data to frontend
import com.example.studyup.dto.SectionDto;

// CourseService contains the business logic
import com.example.studyup.service.CourseService;

// Lombok annotation to auto-generate constructor
import lombok.RequiredArgsConstructor;

// Spring annotations for REST APIs
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Marks this class as a REST controller
// It receives requests and returns JSON responses
@RestController

// Base URL for all endpoints in this controller
// Example URLs:
// /api/courses
// /api/courses/1/sections
@RequestMapping("/api/courses")

// Lombok creates a constructor for all final fields
// So we don’t need to write it manually
@RequiredArgsConstructor
public class CourseController {

    // CourseService is injected here
    // Controller uses service to get data
    private final CourseService courseService;

    // ------------------------------------
    // GET /api/courses
    // Returns all courses
    // ------------------------------------
    @GetMapping
    public List<CourseDto> getAllCourses() {

        // Calls service method to get all courses
        // Service fetches data from database and converts to DTOs
        return courseService.getAllCourses();
    }

    // ------------------------------------
    // GET /api/courses/{courseId}/sections
    // Returns sections for a specific course
    // ------------------------------------
    @GetMapping("/{courseId}/sections")
    public List<SectionDto> getSectionsByCourse(

            // @PathVariable gets courseId from URL
            // Example URL: /api/courses/5/sections
            @PathVariable Long courseId
    ) {

        // Calls service to get sections for the given course
        return courseService.getSectionsForCourse(courseId);
    }
}

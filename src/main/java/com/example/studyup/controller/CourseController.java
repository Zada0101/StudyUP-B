package com.example.studyup.controller;

import com.example.studyup.dto.CourseDto;
import com.example.studyup.dto.SectionDto;
import com.example.studyup.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // GET /api/courses
    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    // GET /api/courses/{courseId}/sections
    @GetMapping("/{courseId}/sections")
    public List<SectionDto> getSectionsByCourse(@PathVariable Long courseId) {
        return courseService.getSectionsForCourse(courseId);
    }
}

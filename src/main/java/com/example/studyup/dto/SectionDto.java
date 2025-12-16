// This package shows that this class is a DTO (Data Transfer Object)
// DTOs are used to send data between backend and frontend
package com.example.studyup.dto;

// Lombok annotations to reduce boilerplate code
import lombok.*;

// @Getter automatically creates getter methods for all fields
@Getter

// @Setter automatically creates setter methods for all fields
@Setter

// @NoArgsConstructor creates an empty constructor
// Needed for JSON deserialization
@NoArgsConstructor

// @AllArgsConstructor creates a constructor with all fields
// Example: new SectionDto(id, content)
@AllArgsConstructor

// @Builder allows creating objects using the builder pattern
// Example:
// SectionDto section = SectionDto.builder()
//     .id(1L)
//     .content("Introduction to Spring Boot")
//     .build();
@Builder
public class SectionDto {

    // Unique ID of the section
    private Long id;

    // Content/text of the section
    // Example: lesson text or description
    private String content;
}

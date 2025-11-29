package com.example.studyup.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

/**
 * Standard API error response structure.
 */
@Getter
@Setter
public class ErrorResponse {

    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String path;
    private Map<String, String> fieldErrors;
}

package com.example.studyup.dto; // ✅ Fixed: lowercase package name

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

/**
 * ✅ ErrorResponse
 * A standard structure for returning API error details.
 *
 * Fields:
 *  - timestamp: when the error occurred
 *  - status: HTTP status code
 *  - error: brief message about the error
 *  - path: the request path that caused the error
 *  - fieldErrors: validation errors for specific fields (if any)
 */
@Getter
@Setter // ✅ Add this so you don’t need manual setters
public class ErrorResponse {

    private Instant timestamp = Instant.now(); // When it happened
    private int status;                        // HTTP status code
    private String error;                      // Short message
    private String path;                       // Request path
    private Map<String, String> fieldErrors;   // Field -> message

    // ✅ No need for manual setters thanks to Lombok @Setter
}

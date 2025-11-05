package com.example.studyup.exception; // Exception package


import com.example.studyup.dto.ErrorResponse; // Error body
import org.springframework.http.HttpStatus; // Status codes
import org.springframework.http.ResponseEntity; // Response builder
import org.springframework.web.bind.MethodArgumentNotValidException; // Validation errors
import org.springframework.web.bind.annotation.ControllerAdvice; // Global handler
import org.springframework.web.bind.annotation.ExceptionHandler; // Handle specific exceptions
import org.springframework.web.context.request.ServletWebRequest; // Access request info


import java.util.LinkedHashMap; // Ordered map
import java.util.stream.Collectors; // Collectors


@ControllerAdvice // Apply to all controllers
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class) // Bean validation failure
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, ServletWebRequest req) {
        var fieldMap = ex.getBindingResult().getFieldErrors().stream() // For each field error
                .collect(Collectors.toMap(f -> f.getField(), f -> f.getDefaultMessage(), (a,b)->a, LinkedHashMap::new)); // Build map


        var err = new ErrorResponse(); // Our error shape
        err.setStatus(HttpStatus.BAD_REQUEST.value()); // 400
        err.setError("Validation failed"); // Message
        err.setPath(req.getRequest().getRequestURI()); // Path
        err.setFieldErrors(fieldMap); // Field messages
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); // Return 400 JSON
    }


    @ExceptionHandler(IllegalArgumentException.class) // Our simple business errors
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex, ServletWebRequest req) {
        var err = new ErrorResponse();
        err.setStatus(HttpStatus.BAD_REQUEST.value()); // 400 for now
        err.setError(ex.getMessage()); // Show message
        err.setPath(req.getRequest().getRequestURI()); // Path
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
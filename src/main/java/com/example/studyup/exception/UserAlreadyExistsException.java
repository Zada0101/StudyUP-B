package com.example.studyup.exception;

/**
 * ✅ Custom exception thrown when a user tries to register with an existing username.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

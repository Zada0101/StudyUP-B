package com.example.studyup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the AI-generated reply and the session ID.
 * Returned to the frontend every time the user sends a message.
 *
 * Example:
 * {
 *     "reply": "Sure, I can help!",
 *     "sessionId": 12
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String reply;
    private Long sessionId;  // <-- REQUIRED for persistent chat
}

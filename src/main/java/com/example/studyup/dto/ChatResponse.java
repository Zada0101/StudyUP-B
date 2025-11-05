package com.example.studyup.dto; // ✅ Lowercase package name for consistency

import lombok.Getter;
import lombok.Setter;

/**
 * ✅ ChatResponse DTO
 * Represents a simple response returned from the ChatController.
 */
@Getter
@Setter
public class ChatResponse {

    /** 💬 The AI-generated reply message */
    private String reply;

    // ✅ Default constructor (needed for JSON serialization)
    public ChatResponse() {
    }

    // ✅ Convenient constructor for direct initialization
    public ChatResponse(String reply) {
        this.reply = reply;
    }
}

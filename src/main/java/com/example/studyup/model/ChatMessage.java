// This package shows that this class is a MODEL / ENTITY
// Entities represent database tables
package com.example.studyup.model;

// JPA annotations used for database mapping
import jakarta.persistence.*;

// Lombok annotations to reduce boilerplate code
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

// @Entity tells JPA that this class is a database table
@Entity

// @Data automatically generates getters, setters,
// toString(), equals(), and hashCode()
@Data

// @Builder allows creating objects using builder pattern
// Example:
// ChatMessage msg = ChatMessage.builder()
//     .content("Hello")
//     .role("user")
//     .build();
@Builder

// @NoArgsConstructor creates an empty constructor
// Required by JPA
@NoArgsConstructor

// @AllArgsConstructor creates a constructor with all fields
@AllArgsConstructor

// Defines the database table name
@Table(name = "chat_messages")
public class ChatMessage {

    // @Id marks this field as the primary key
    @Id

    // @GeneratedValue auto-generates ID values
    // IDENTITY means database handles ID creation
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The actual message text
    // Example: "Hello, explain Spring Boot"
    private String content;

    // Who sent the message
    // "user" → message from user
    // "assistant" → message from AI
    private String role; // "user" or "assistant"

    // Old option using LocalDateTime (not used now)
//    private LocalDateTime createdAt;

    // Time when the message was created
    // Instant stores exact timestamp
    private Instant createdAt;

    // Many-to-One relationship:
    // Many chat messages belong to ONE chat session
    @ManyToOne

    // Defines the foreign key column in chat_messages table
    // session_id links message to chat session
    @JoinColumn(name = "session_id")
    private ChatSession session;
}

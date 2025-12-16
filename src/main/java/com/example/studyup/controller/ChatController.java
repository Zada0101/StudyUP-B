// This package means: this class is a CONTROLLER
// Controllers receive HTTP requests from frontend
package com.example.studyup.controller;

// DTO: data coming from frontend when user sends a chat message
import com.example.studyup.dto.ChatRequest;

// DTO: data sent back to frontend as chat response
import com.example.studyup.dto.ChatResponse;

// AppUser = logged-in user entity from database
import com.example.studyup.model.AppUser;

// ChatMessage = one message in a chat (user or AI)
import com.example.studyup.model.ChatMessage;

// ChatSession = one chat conversation (like one chat history)
import com.example.studyup.model.ChatSession;

// Repositories = talk to database
import com.example.studyup.repository.ChatMessageRepository;
import com.example.studyup.repository.ChatSessionRepository;
import com.example.studyup.repository.UserRepository;

// ChatService contains main chat logic (AI call, save messages, etc.)
import com.example.studyup.service.ChatService;

// Lombok: automatically creates constructor & getters
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Used to return HTTP responses
import org.springframework.http.ResponseEntity;

// Authentication gives info about logged-in user
import org.springframework.security.core.Authentication;

// REST annotations
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Marks this class as a REST API controller
@RestController

// Base URL for all chat endpoints
// Example:
// /api/chat
// /api/chat/sessions
@RequestMapping("/api/chat")

// Lombok: generates constructor for all final fields
@RequiredArgsConstructor

// Allows React frontend to call this backend (CORS)
@CrossOrigin(
        origins = {
                "http://localhost:5175",
                "http://127.0.0.1:5174",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        },
        allowCredentials = "true"
)
public class ChatController {

    // Service that handles chat logic
    private final ChatService chatService;

    // Repository for chat sessions
    private final ChatSessionRepository sessionRepo;

    // Repository for chat messages
    private final ChatMessageRepository messageRepo;

    // Repository for users
    private final UserRepository userRepository;

    // --------------------------------------------------------
    // Helper method: get the currently logged-in user
    // --------------------------------------------------------
    private AppUser getLoggedUser(Authentication auth) {

        // auth.getName() = username of logged-in user
        // Find user in database by username
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + auth.getName()));
    }

    // --------------------------------------------------------
    // 1) POST /api/chat
    // User sends message → AI replies
    // --------------------------------------------------------
    @PostMapping
    public ResponseEntity<ChatResponse> chat(

            // Request body from frontend (user message, session id, etc.)
            @RequestBody ChatRequest request,

            // Authentication object (who is logged in)
            Authentication authentication
    ) {

        // Call service to handle chat logic
        // (save message, call AI, save reply)
        ChatResponse response = chatService.handleChat(request);

        // Return response with HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------------
    // 2) GET /api/chat/sessions
    // Get list of user's chat sessions
    // --------------------------------------------------------
    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionSummaryDto>> listSessions(
            Authentication auth
    ) {

        // Get logged-in user
        AppUser user = getLoggedUser(auth);

        // Fetch sessions for this user from database
        // Ordered by last updated
        List<ChatSession> sessions =
                sessionRepo.findByUserOrderByUpdatedAtDesc(user);

        // Convert ChatSession entities to DTOs
        List<ChatSessionSummaryDto> dtos =
                sessions.stream()
                        .map(s -> new ChatSessionSummaryDto(
                                s.getId(),
                                s.getTitle(),
                                s.getCreatedAt(),
                                s.getUpdatedAt()
                        ))
                        .collect(Collectors.toList());

        // Return list of sessions
        return ResponseEntity.ok(dtos);
    }

    // --------------------------------------------------------
    // 3) GET /api/chat/sessions/{id}
    // Get full chat conversation for one session
    // --------------------------------------------------------
    @GetMapping("/sessions/{id}")
    public ResponseEntity<ChatSessionDetailDto> getSession(

            // Session ID from URL
            @PathVariable Long id,

            // Logged-in user info
            Authentication auth
    ) {

        // Get logged-in user
        AppUser user = getLoggedUser(auth);

        // Find chat session by ID
        ChatSession session = sessionRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Session not found"));

        // Security check:
        // user can only access their own session
        if (!session.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        // Fetch all messages in this session
        List<ChatMessage> messages =
                messageRepo.findBySessionOrderByCreatedAtAsc(session);

        // Convert messages to DTOs
        List<ChatMessageDto> msgDtos =
                messages.stream()
                        .map(m -> new ChatMessageDto(
                                m.getId(),
                                m.getRole(),       // user or assistant
                                m.getContent(),    // message text
                                m.getCreatedAt()
                        ))
                        .collect(Collectors.toList());

        // Return full session details
        return ResponseEntity.ok(
                new ChatSessionDetailDto(
                        session.getId(),
                        session.getTitle(),
                        session.getCreatedAt(),
                        session.getUpdatedAt(),
                        msgDtos
                )
        );
    }

    // --------------------------------------------------------
    // 4) DELETE /api/chat/sessions/{id}
    // Delete a chat session
    // --------------------------------------------------------
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(

            // Session ID from URL
            @PathVariable Long id,

            // Logged-in user info
            Authentication auth
    ) {

        // Get logged-in user
        AppUser user = getLoggedUser(auth);

        // Find session
        ChatSession session = sessionRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Session not found"));

        // Check ownership
        if (!session.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        // Delete session from database
        sessionRepo.delete(session);

        // Return success message
        return ResponseEntity.ok(Map.of("message", "deleted"));
    }

    // --------------------------------------------------------
    // DTO CLASSES (used only for responses)
    // --------------------------------------------------------

    // Simple session info (for session list)
    @Getter
    @AllArgsConstructor
    static class ChatSessionSummaryDto {
        private Long id;
        private String title;
        private Instant createdAt;
        private Instant updatedAt;
    }

    // One message DTO
    @Getter
    @AllArgsConstructor
    static class ChatMessageDto {
        private Long id;
        private String role;      // user / assistant
        private String content;
        private Instant createdAt;
    }

    // Full session with all messages
    @Getter
    @AllArgsConstructor
    static class ChatSessionDetailDto {
        private Long id;
        private String title;
        private Instant createdAt;
        private Instant updatedAt;
        private List<ChatMessageDto> messages;
    }
}

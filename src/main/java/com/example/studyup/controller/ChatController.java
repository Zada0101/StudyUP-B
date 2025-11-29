package com.example.studyup.controller;

import com.example.studyup.dto.ChatRequest;
import com.example.studyup.dto.ChatResponse;
import com.example.studyup.model.AppUser;
import com.example.studyup.model.ChatMessage;
import com.example.studyup.model.ChatSession;
import com.example.studyup.repository.ChatMessageRepository;
import com.example.studyup.repository.ChatSessionRepository;
import com.example.studyup.repository.UserRepository;
import com.example.studyup.service.ChatService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        },
        allowCredentials = "true"
)
public class ChatController {

    private final ChatService chatService;
    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;
    private final UserRepository userRepository;

    // --------------------------------------------------------
    // Helper: get logged-in user
    // --------------------------------------------------------
    private AppUser getLoggedUser(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + auth.getName()));
    }

    // --------------------------------------------------------
    // 1) POST /api/chat  → send message + AI reply
    // --------------------------------------------------------
    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request,
            Authentication authentication
    ) {
        ChatResponse response = chatService.handleChat(request);
        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------------
    // 2) GET /api/chat/sessions  → list user sessions
    // --------------------------------------------------------
    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionSummaryDto>> listSessions(Authentication auth) {
        AppUser user = getLoggedUser(auth);

        List<ChatSession> sessions =
                sessionRepo.findByUserOrderByUpdatedAtDesc(user);

        List<ChatSessionSummaryDto> dtos =
                sessions.stream()
                        .map(s -> new ChatSessionSummaryDto(
                                s.getId(),
                                s.getTitle(),
                                s.getCreatedAt(),
                                s.getUpdatedAt()
                        ))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // --------------------------------------------------------
    // 3) GET /api/chat/sessions/{id}  → full conversation
    // --------------------------------------------------------
    @GetMapping("/sessions/{id}")
    public ResponseEntity<ChatSessionDetailDto> getSession(
            @PathVariable Long id,
            Authentication auth
    ) {
        AppUser user = getLoggedUser(auth);

        ChatSession session = sessionRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        List<ChatMessage> messages =
                messageRepo.findBySessionOrderByCreatedAtAsc(session);

        List<ChatMessageDto> msgDtos =
                messages.stream()
                        .map(m -> new ChatMessageDto(
                                m.getId(),
                                m.getRole(),
                                m.getContent(),
                                m.getCreatedAt()
                        ))
                        .collect(Collectors.toList());

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
    // 4) DELETE /api/chat/sessions/{id}  → delete a session
    // --------------------------------------------------------
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(
            @PathVariable Long id,
            Authentication auth
    ) {
        AppUser user = getLoggedUser(auth);

        ChatSession session = sessionRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        sessionRepo.delete(session);

        return ResponseEntity.ok(Map.of("message", "deleted"));
    }

    // --------------------------------------------------------
    // DTOs
    // --------------------------------------------------------
    @Getter
    @AllArgsConstructor
    static class ChatSessionSummaryDto {
        private Long id;
        private String title;
        private Instant createdAt;
        private Instant updatedAt;
    }

    @Getter
    @AllArgsConstructor
    static class ChatMessageDto {
        private Long id;
        private String role;
        private String content;
        private Instant createdAt;
    }

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

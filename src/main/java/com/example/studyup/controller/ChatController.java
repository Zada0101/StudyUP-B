package com.example.studyup.controller;

import com.example.studyup.dto.ChatRequest;
import com.example.studyup.dto.ChatResponse;
import com.example.studyup.model.ChatMessage;
import com.example.studyup.model.ChatSession;
import com.example.studyup.model.Section;
import com.example.studyup.repository.ChatMessageRepository;
import com.example.studyup.repository.ChatSessionRepository;
import com.example.studyup.repository.SectionRepository;
import com.example.studyup.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin
public class ChatController {

    private final ChatService chatService;
    private final SectionRepository sectionRepo;
    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;

    // 1️⃣ Send message
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        Section section = sectionRepo
                .findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found"));

        return chatService.chat(request, section.getContent());
    }

    // 2️⃣ Get chat history
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getHistory(
            @PathVariable Long sessionId
    ) {
        ChatSession session = sessionRepo.findById(sessionId).orElse(null);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        List<ChatMessage> messages =
                messageRepo.findBySessionOrderByCreatedAtAsc(session);

        return ResponseEntity.ok(messages);
    }

    // 3️⃣ Get sessions for a section (LEFT SIDEBAR)
    @GetMapping("/sessions/{sectionId}")
    public List<ChatSession> getSessions(@PathVariable String sectionId) {
        return sessionRepo.findBySectionIdOrderByUpdatedAtDesc(sectionId);
    }

    // 4️⃣ Delete session (NEW CHAT)
    @DeleteMapping("/history/{sessionId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long sessionId) {
        ChatSession session = sessionRepo.findById(sessionId).orElse(null);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        messageRepo.deleteBySession(session);
        sessionRepo.delete(session);
        return ResponseEntity.noContent().build();
    }
}

package com.example.studyup.service;

import com.example.studyup.dto.ChatRequest;
import com.example.studyup.dto.ChatResponse;
import com.example.studyup.model.AppUser;
import com.example.studyup.model.ChatMessage;
import com.example.studyup.model.ChatSession;
import com.example.studyup.repository.ChatMessageRepository;
import com.example.studyup.repository.ChatSessionRepository;
import com.example.studyup.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAIService openAIService;
    private final UserRepository userRepository;
    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;

    // -------------------------------------------------------
    // MAIN CHAT HANDLER
    // -------------------------------------------------------
    public ChatResponse handleChat(ChatRequest request) {

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return new ChatResponse("❌ Error: message cannot be empty.", null);
        }

        AppUser user = getLoggedUser();
        ChatSession session;

        // ---------------- NEW SESSION ----------------
        if (request.getSessionId() == null) {
            session = new ChatSession();
            session.setUser(user);
            session.setTitle(generateTitle(request.getMessage()));
            session.setCreatedAt(Instant.now());
            session.setUpdatedAt(Instant.now());
            session = sessionRepo.save(session);
        }
        // ---------------- EXISTING SESSION ----------------
        else {
            session = sessionRepo.findById(request.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            if (!session.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Unauthorized session access");
            }

            session.setUpdatedAt(Instant.now());
            sessionRepo.save(session);
        }

        // ---------------- SAVE USER MESSAGE ----------------
        ChatMessage userMsg = new ChatMessage();
        userMsg.setContent(request.getMessage());
        userMsg.setRole("user");
        userMsg.setSession(session);
        userMsg.setCreatedAt(Instant.now());
        messageRepo.save(userMsg);

        // ---------------- LOAD HISTORY ----------------
        List<ChatMessage> history =
                messageRepo.findBySessionOrderByCreatedAtAsc(session);

        // ---------------- GET AI RESPONSE ----------------
        String aiReply = openAIService.sendMessageWithHistory(history);

        // ---------------- SAVE AI MESSAGE ----------------
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setContent(aiReply);
        aiMsg.setRole("ai");
        aiMsg.setSession(session);
        aiMsg.setCreatedAt(Instant.now());
        messageRepo.save(aiMsg);

        return new ChatResponse(aiReply, session.getId());
    }

    // -------------------------------------------------------
    // DELETE SESSION (NEW)
    // -------------------------------------------------------
    public void deleteSession(Long id) {
        AppUser user = getLoggedUser();

        ChatSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        sessionRepo.delete(session);  // Cascade deletes all messages
    }

    // -------------------------------------------------------
    // HELPERS
    // -------------------------------------------------------
    private AppUser getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + auth.getName()));
    }

    private String generateTitle(String msg) {
        msg = msg.trim();
        return msg.length() <= 40
                ? msg
                : msg.substring(0, 40) + "...";
    }
}

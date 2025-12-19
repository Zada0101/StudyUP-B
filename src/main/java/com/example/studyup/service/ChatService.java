// This package shows that this class belongs to the SERVICE layer
//  contains business logic
package com.example.studyup.service;

// DTO received from frontend (user message + session id)
import com.example.studyup.dto.ChatRequest;

// DTO sent back to frontend (AI reply + session id)
import com.example.studyup.dto.ChatResponse;

// User entity (logged-in user)
import com.example.studyup.model.AppUser;

// Entity representing a single chat message
import com.example.studyup.model.ChatMessage;

// Entity representing a chat session (conversation)
import com.example.studyup.model.ChatSession;

// Repositories used to access database
import com.example.studyup.repository.ChatMessageRepository;
import com.example.studyup.repository.ChatSessionRepository;
import com.example.studyup.repository.UserRepository;

// Lombok annotation to auto-generate constructor for final fields
import lombok.RequiredArgsConstructor;

// Spring Security classes to get logged-in user
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Marks this class as a Spring service
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

// @Service tells Spring this class contains business logic
@Service

// @RequiredArgsConstructor injects all final fields automatically
@RequiredArgsConstructor
public class ChatService {

    // Service that talks to OpenAI / AI API
    private final OpenAIService openAIService;

    // Repository to access users
    private final UserRepository userRepository;

    // Repository to access chat sessions
    private final ChatSessionRepository sessionRepo;

    // Repository to access chat messages
    private final ChatMessageRepository messageRepo;

    // -------------------------------------------------------
    // MAIN CHAT HANDLER
    // This method handles:
    // - creating sessions
    // - saving messages
    // - calling AI
    // - returning response
    // -------------------------------------------------------
    public ChatResponse handleChat(ChatRequest request) {

        // Validate: message must not be empty
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return new ChatResponse("❌ Error: message cannot be empty.", null);
        }

        // Get currently logged-in user
        AppUser user = getLoggedUser();

        // Chat session variable
        ChatSession session;

        // ---------------- NEW SESSION ----------------
        // If sessionId is null → start a new chat
        if (request.getSessionId() == null) {

            // Create new chat session
            session = new ChatSession();
            session.setUser(user);

            // Generate session title from first message
            session.setTitle(generateTitle(request.getMessage()));

            // Set creation and update timestamps
            session.setCreatedAt(Instant.now());
            session.setUpdatedAt(Instant.now());

            // Save session to database
            session = sessionRepo.save(session);
        }

        // ---------------- EXISTING SESSION ----------------
        // If sessionId exists → continue old chat
        else {

            // Load existing session from database
            session = sessionRepo.findById(request.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            // Security check:
            // user can only access their own session
            if (!session.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Unauthorized session access");
            }

            // Update last activity time
            session.setUpdatedAt(Instant.now());

            // Save updated session
            sessionRepo.save(session);
        }

        // ---------------- SAVE USER MESSAGE ----------------
        // Create chat message from user
        ChatMessage userMsg = new ChatMessage();
        userMsg.setContent(request.getMessage());
        userMsg.setRole("user");           // sender is user
        userMsg.setSession(session);
        userMsg.setCreatedAt(Instant.now());

        // Save user message to database
        messageRepo.save(userMsg);

        // ---------------- LOAD HISTORY ----------------
        // Load all messages for this session (oldest → newest)
        List<ChatMessage> history =
                messageRepo.findBySessionOrderByCreatedAtAsc(session);

        // ---------------- GET AI RESPONSE ----------------
        // Send chat history to AI and get reply
        String aiReply = openAIService.sendMessageWithHistory(history);

        // ---------------- SAVE AI MESSAGE ----------------
        // Create chat message from AI
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setContent(aiReply);
        aiMsg.setRole("ai");              // sender is AI
        aiMsg.setSession(session);
        aiMsg.setCreatedAt(Instant.now());

        // Save AI message to database
        messageRepo.save(aiMsg);

        // Return AI reply + session ID to frontend
        return new ChatResponse(aiReply, session.getId());
    }

    // -------------------------------------------------------
    // DELETE SESSION
    // Deletes a chat session and all its messages
    // -------------------------------------------------------
    public void deleteSession(Long id) {

        // Get logged-in user
        AppUser user = getLoggedUser();

        // Find session by ID
        ChatSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Security check: user owns this session
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // Delete session
        // CascadeType.ALL → deletes all messages too
        sessionRepo.delete(session);
    }

    // -------------------------------------------------------
    // HELPER METHODS
    // -------------------------------------------------------

    // Get the currently logged-in user from Spring Security
    private AppUser getLoggedUser() {

        // Get authentication object
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Find user in database by username
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + auth.getName()));
    }

    // Generate a short title for chat session
    // Uses first 40 characters of the first message
    private String generateTitle(String msg) {
        msg = msg.trim();

        // If message is short → use it directly
        // If long → cut and add "..."
        return msg.length() <= 40
                ? msg
                : msg.substring(0, 40) + "...";
    }
}

package com.example.studyup.controller;

import com.example.studyup.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // ✅ Constructor injection
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // ✅ Simple synchronous endpoint
    @PostMapping
    public String chat(@RequestBody String message) {
        return chatService.chatWithAI(message);
    }
}

package com.example.studyup.controller;

import com.example.studyup.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OpenAIService openAIService;

    // POST /api/chat
    @PostMapping
    public Mono<Map<String, Object>> chatPost(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        return openAIService.sendMessage(message)
                .map(reply -> Map.of("reply", reply));
    }

    // GET /api/chat?message=hello
    @GetMapping
    public Mono<Map<String, Object>> chatGet(@RequestParam String message) {
        return openAIService.sendMessage(message)
                .map(reply -> Map.of("reply", reply));
    }
}

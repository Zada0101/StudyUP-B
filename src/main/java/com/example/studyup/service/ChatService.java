package com.example.studyup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAIService openAIService;

    public Mono<String> sendMessage(String message) {
        return openAIService.sendMessage(message);
    }
}

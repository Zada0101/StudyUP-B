package com.example.studyup.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * ✅ ChatService demonstrates async call using WebClient.
 * For now, it mocks simple AI chat behavior.
 *
 * Later, you can connect this to a real external API
 * (like OpenAI or your own AI microservice).
 */
@Getter
@Service
public class ChatService {

    // ✅ Declare WebClient as a class field (so we can reuse it)
    private final WebClient webClient;

    // ✅ Initialize WebClient once via constructor
    public ChatService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * ✅ Simple synchronous chat mock method.
     * In a real app, this would make a network request.
     */
    public String chatWithAI(String message) {
        return "Echo: " + message; // returns a simple response
    }

    /**
     * ✅ Example asynchronous version using Reactor.
     * Returns a Mono<String> (reactive type) instead of plain String.
     */
    public Mono<String> chatAsync(String message) {
        return Mono.just("Echo async: " + message);
    }

}

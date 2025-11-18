package com.example.studyup.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();

    public Mono<String> sendMessage(String message) {
        return Mono.fromCallable(() -> {

            MediaType mediaType = MediaType.parse("application/json");

            String jsonBody = """
                    {
                      "model": "gpt-5.1-chat-latest",
                      "messages": [
                        { "role": "user", "content": "%s" }
                      ]
                    }
                    """.formatted(message);

            RequestBody body = RequestBody.create(jsonBody, mediaType);

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (!response.isSuccessful()) {
                    return "Error: " + response.code() + " - " + response.message();
                }

                String responseBody = response.body().string();

                JSONObject json = new JSONObject(responseBody);
                JSONArray choices = json.getJSONArray("choices");

                if (choices.isEmpty()) {
                    return "No response from OpenAI.";
                }

                return choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }
        });
    }
}

package com.example.studyup.service;

import com.example.studyup.model.ChatMessage;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAIService {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    // ✅ FIXED — increase timeouts (default OkHttp is too short)
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    // =========================================================================
    // 1️⃣ SIMPLE MESSAGE TO OPENAI
    // =========================================================================
    public String sendMessage(String message) {
        if (!isApiKeyValid()) return missingKeyError();

        try {
            JSONObject payload = new JSONObject()
                    .put("model", "gpt-4o-mini")
                    .put("messages", new JSONArray()
                            .put(new JSONObject()
                                    .put("role", "user")
                                    .put("content", message)));

            Request request = buildRequest(payload);

            try (Response response = client.newCall(request).execute()) {
                return extractResponse(response);
            }

        } catch (Exception e) {
            return "❌ ERROR contacting OpenAI: " + e.getMessage();
        }
    }

    // =========================================================================
    // 2️⃣ MULTI-TURN CHAT USING HISTORY
    // =========================================================================
    public String sendMessageWithHistory(List<ChatMessage> history) {
        if (!isApiKeyValid()) return missingKeyError();

        try {
            JSONArray messagesJson = new JSONArray();

            for (ChatMessage msg : history) {
                messagesJson.put(new JSONObject()
                        .put("role",
                                msg.getRole().equalsIgnoreCase("ai") ? "assistant" : "user")
                        .put("content", msg.getContent()));
            }

            JSONObject payload = new JSONObject()
                    .put("model", "gpt-4o-mini")
                    .put("messages", messagesJson);

            Request request = buildRequest(payload);

            try (Response response = client.newCall(request).execute()) {
                return extractResponse(response);
            }

        } catch (Exception e) {
            return "❌ ERROR contacting OpenAI: " + e.getMessage();
        }
    }

    // =========================================================================
    // Helper: Build request for OpenAI
    // =========================================================================
    private Request buildRequest(JSONObject payload) {

        RequestBody body = RequestBody.create(
                payload.toString(),
                MediaType.parse("application/json")
        );

        // ⚠ IMPORTANT — new API requires OpenAI-Beta header for 4o models
        return new Request.Builder()
                .url(OPENAI_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("OpenAI-Beta", "assistants=v2")
                .header("Content-Type", "application/json")
                .post(body)
                .build();
    }

    // =========================================================================
    // Helper: Extract content from OpenAI response
    // =========================================================================
    private String extractResponse(Response response) throws Exception {

        if (!response.isSuccessful()) {
            return "❌ OpenAI Error: " + response.code() + " - " + response.message();
        }

        if (response.body() == null) {
            return "❌ ERROR: Empty AI response body.";
        }

        String raw = response.body().string();
        System.out.println("🔍 RAW OPENAI RESPONSE:\n" + raw);

        JSONObject json = new JSONObject(raw);

        return json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    // =========================================================================
    // API KEY CHECK
    // =========================================================================
    private boolean isApiKeyValid() {
        return apiKey != null && !apiKey.isBlank();
    }

    private String missingKeyError() {
        return "❌ ERROR: Missing OpenAI API key.\n" +
                "➡ Add to application.properties:\n" +
                "openai.api.key=YOUR_KEY_HERE";
    }
}

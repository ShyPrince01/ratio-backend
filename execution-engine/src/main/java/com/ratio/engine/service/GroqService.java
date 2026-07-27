package com.ratio.engine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratio.engine.dto.ExecutionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GroqService {

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.model:llama3-8b-8192}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public ExecutionResponse generateResponse(String prompt, String userModel, int maxTokens) {
        String selectedModel = userModel != null ? mapModel(userModel) : model;

        System.err.println("=== Groq Debug ===");
        System.err.println("API Key present: " + (apiKey != null && !apiKey.isEmpty()));
        System.err.println("API Key starts with: " + (apiKey != null && apiKey.length() > 4 ? apiKey.substring(0, 4) + "..." : "empty"));
        System.err.println("Model: " + selectedModel);

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("model", selectedModel);
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> msg = new HashMap<>();
            msg.put("role", "user");
            msg.put("content", prompt);
            messages.add(msg);
            body.put("messages", messages);
            body.put("max_tokens", maxTokens);
            body.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    request,
                    String.class
            );

            JsonNode root = mapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();
            int promptTokens = root.path("usage").path("prompt_tokens").asInt();
            int completionTokens = root.path("usage").path("completion_tokens").asInt();

            System.err.println("=== Groq Success ===");
            System.err.println("Response: " + content.substring(0, Math.min(100, content.length())) + "...");

            ExecutionResponse.UsageInfo usage = new ExecutionResponse.UsageInfo(promptTokens, completionTokens);
            return new ExecutionResponse(content, selectedModel, usage);

        } catch (Exception e) {
            System.err.println("=== Groq Error ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            MockLLMService mock = new MockLLMService();
            return mock.generateResponse(prompt, selectedModel, maxTokens);
        }
    }

    private String mapModel(String model) {
        return switch (model) {
            case "gpt-4o" -> "llama3-70b-8192";
            case "gpt-4o-mini" -> "llama3-8b-8192";
            case "gpt-3.5-turbo" -> "llama3-8b-8192";
            default -> "llama3-8b-8192";
        };
    }
}
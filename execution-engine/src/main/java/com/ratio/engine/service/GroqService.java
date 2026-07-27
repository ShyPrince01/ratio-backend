package com.ratio.engine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratio.engine.dto.ExecutionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GroqService {

    private static final Logger log = LoggerFactory.getLogger(GroqService.class);

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.model:llama3-8b-8192}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public ExecutionResponse generateResponse(String prompt, String userModel, int maxTokens) {
        String selectedModel = userModel != null ? mapModel(userModel) : model;

        log.error("=== Groq Debug ===");
        log.error("API Key present: {}", (apiKey != null && !apiKey.isEmpty()));
        log.error("API Key starts with: {}", (apiKey != null && apiKey.length() > 4 ? apiKey.substring(0, 4) + "..." : "empty"));
        log.error("Model: {}", selectedModel);

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

            log.error("=== Groq Success ===");

            ExecutionResponse.UsageInfo usage = new ExecutionResponse.UsageInfo(promptTokens, completionTokens);
            return new ExecutionResponse(content, selectedModel, usage);

        } catch (Exception e) {
            log.error("=== Groq Error ===");
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Groq API failed: " + e.getMessage(), e);
        }
    }

    private String mapModel(String model) {
        return switch (model) {
            case "gpt-4o" -> "llama-3.3-70b-versatile";
            case "gpt-4o-mini" -> "llama-3.1-8b-instant";
            case "gpt-3.5-turbo" -> "llama-3.1-8b-instant";
            default -> "llama-3.1-8b-instant";
        };
    }
}
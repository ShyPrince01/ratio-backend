package com.ratio.engine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CostTrackerClient {
    
    private static final Logger log = LoggerFactory.getLogger(CostTrackerClient.class);
    private final RestTemplate restTemplate;
    
    @Value("${services.cost-tracker.url}")
    private String costTrackerUrl;
    
    public CostTrackerClient() {
        this.restTemplate = new RestTemplate();
    }
    
    public void trackUsage(String promptId, String model, int promptTokens, 
                          int completionTokens, int totalTokens) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> body = new HashMap<>();
            body.put("promptId", promptId);
            body.put("model", model);
            body.put("promptTokens", promptTokens);
            body.put("completionTokens", completionTokens);
            body.put("totalTokens", totalTokens);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            
            restTemplate.postForEntity(
                costTrackerUrl + "/track", 
                request, 
                String.class
            );
            
            log.info("Successfully tracked usage for prompt: {}", promptId);
        } catch (Exception e) {
            // Graceful degradation - don't fail execution if cost tracking fails
            log.error("Failed to track usage (non-critical): {}", e.getMessage());
        }
    }
}
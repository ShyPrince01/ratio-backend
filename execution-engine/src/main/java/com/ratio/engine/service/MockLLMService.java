package com.ratio.engine.service;

import com.ratio.engine.dto.ExecutionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MockLLMService {
    
    private final Random random = new Random();
    
    @Value("${llm.mock.simulated-latency-ms:1500}")
    private long simulatedLatency;
    
    // Predefined responses to simulate varied LLM outputs
    private static final String[] RESPONSES = {
        "Based on the analysis, the key performance indicators show a 23% improvement in customer satisfaction scores. The implementation of the new feedback system has resulted in faster response times and more accurate issue resolution.",
        
        "I recommend implementing a phased approach to the digital transformation strategy. Start with customer-facing applications, then move to internal processes. This minimizes disruption while maximizing early wins.",
        
        "The cost-benefit analysis reveals that automation could save approximately $450,000 annually. The initial investment of $120,000 would be recovered within the first quarter of operation.",
        
        "After reviewing the customer feedback data, three main pain points emerged: response time (45% of complaints), product documentation (30%), and billing clarity (25%). Addressing these in order would yield the highest ROI.",
        
        "The market analysis indicates a growing demand for AI-powered solutions in the healthcare sector, with projected CAGR of 34.5% over the next five years. Early market entry could capture significant market share."
    };
    
    public ExecutionResponse generateResponse(String prompt, String model, int maxTokens) {
        // Simulate processing time
        try {
            Thread.sleep(simulatedLatency);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Pick a random response
        String response = RESPONSES[random.nextInt(RESPONSES.length)];
        
        // Simulate realistic token counts
        int promptTokens = estimateTokens(prompt);
        int completionTokens = estimateTokens(response);
        
        ExecutionResponse.UsageInfo usage = new ExecutionResponse.UsageInfo(promptTokens, completionTokens);
        
        return new ExecutionResponse(response, model, usage);
    }
    
    private int estimateTokens(String text) {
        // Rough estimation: 1 token ≈ 4 characters
        return Math.max(1, text.length() / 4);
    }
}
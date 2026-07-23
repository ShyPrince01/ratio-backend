package com.ratio.engine.controller;

import com.ratio.engine.dto.ExecuteRequest;
import com.ratio.engine.dto.ExecutionResponse;
import com.ratio.engine.service.CostTrackerClient;
import com.ratio.engine.service.MockLLMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/execute")
public class ExecutionController {
    
    private final MockLLMService llmService;
    private final CostTrackerClient costTrackerClient;
    
    public ExecutionController(MockLLMService llmService, CostTrackerClient costTrackerClient) {
        this.llmService = llmService;
        this.costTrackerClient = costTrackerClient;
    }
    
    @PostMapping
    public ResponseEntity<ExecutionResponse> executePrompt(@RequestBody ExecuteRequest request) {
        // In a real implementation, you'd fetch the prompt from prompt-management service
        // if promptId is provided
        String promptContent = request.getContent() != null ? 
            request.getContent() : "Default prompt content";
        
        String model = request.getModel() != null ? request.getModel() : "gpt-4o";
        int maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 500;
        
        // Execute the prompt
        ExecutionResponse response = llmService.generateResponse(promptContent, model, maxTokens);
        
        // Track usage asynchronously (fire and forget)
        costTrackerClient.trackUsage(
            request.getPromptId(),
            response.getModel(),
            response.getUsage().getPromptTokens(),
            response.getUsage().getCompletionTokens(),
            response.getUsage().getTotalTokens()
        );
        
        return ResponseEntity.ok(response);
    }
}
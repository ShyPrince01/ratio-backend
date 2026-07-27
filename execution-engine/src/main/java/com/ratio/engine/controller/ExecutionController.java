package com.ratio.engine.controller;

import com.ratio.engine.dto.ExecuteRequest;
import com.ratio.engine.dto.ExecutionResponse;
import com.ratio.engine.service.CostTrackerClient;
import com.ratio.engine.service.GroqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/execute")
public class ExecutionController {

    private static final Logger log = LoggerFactory.getLogger(ExecutionController.class);
    private final GroqService groqService;
    private final CostTrackerClient costTrackerClient;

    public ExecutionController(GroqService groqService, CostTrackerClient costTrackerClient) {
        this.groqService = groqService;
        this.costTrackerClient = costTrackerClient;
    }

    @PostMapping
    public ResponseEntity<ExecutionResponse> executePrompt(@RequestBody ExecuteRequest request) {
        log.error("=== Controller Hit ===");

        String promptContent = request.getContent() != null
                ? request.getContent() : "Default prompt content";

        String model = request.getModel() != null ? request.getModel() : "gpt-4o";
        int maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 500;
        double temperature = request.getTemperature() != null ? request.getTemperature() : 0.7;

        ExecutionResponse response = groqService.generateResponse(promptContent, model, maxTokens, temperature);

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
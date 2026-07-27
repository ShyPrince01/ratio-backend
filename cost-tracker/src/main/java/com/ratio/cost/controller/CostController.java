package com.ratio.cost.controller;

import java.util.*;
import com.ratio.cost.dto.TrackRequest;
import com.ratio.cost.model.UsageLog;
import com.ratio.cost.repository.UsageLogRepository;
import com.ratio.cost.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cost")
public class CostController {

    private final UsageLogRepository repository;
    private final PricingService pricingService;

    public CostController(UsageLogRepository repository, PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    @PostMapping("/track")
    public ResponseEntity<UsageLog> trackUsage(@Valid @RequestBody TrackRequest request) {
        double cost = pricingService.calculateCost(
                request.getModel(),
                request.getPromptTokens(),
                request.getCompletionTokens()
        );

        UsageLog log = new UsageLog(
                request.getPromptId(),
                request.getModel(),
                request.getPromptTokens(),
                request.getCompletionTokens(),
                request.getTotalTokens(),
                cost
        );

        UsageLog saved = repository.save(log);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/usage")
    public ResponseEntity<Map<String, Object>> getUsage(
            @RequestParam(required = false) String promptId) {

        Map<String, Object> response = new HashMap<>();

        if (promptId != null) {
            List<UsageLog> logs = repository.findByPromptId(promptId);

            double totalCost = logs.stream().mapToDouble(UsageLog::getCost).sum();
            long totalTokens = logs.stream().mapToLong(UsageLog::getTotalTokens).sum();

            response.put("promptId", promptId);
            response.put("executions", logs.size());
            response.put("totalCost", totalCost);
            response.put("totalTokens", totalTokens);
        } else {
            response.put("totalLogs", repository.count());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<UsageLog> all = repository.findAll();

        double totalCost = all.stream().mapToDouble(UsageLog::getCost).sum();
        long totalTokens = all.stream().mapToLong(UsageLog::getTotalTokens).sum();
        long totalPrompts = all.size();

        // Model breakdown
        Map<String, Long> modelCounts = new HashMap<>();
        for (UsageLog log : all) {
            modelCounts.merge(log.getModel(), 1L, Long::sum);
        }

        // Daily costs (last 7 days)
        Map<String, Double> dailyCosts = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            String day = java.time.LocalDate.now().minusDays(i).toString();
            dailyCosts.put(day, 0.0);
        }
        for (UsageLog log : all) {
            String day = log.getCreatedAt().toLocalDate().toString();
            if (dailyCosts.containsKey(day)) {
                dailyCosts.merge(day, log.getCost(), Double::sum);
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPrompts", totalPrompts);
        stats.put("totalTokens", totalTokens);
        stats.put("totalCost", Math.round(totalCost * 10000.0) / 10000.0);
        stats.put("modelBreakdown", modelCounts);
        stats.put("dailyCosts", dailyCosts);

        return ResponseEntity.ok(stats);
    }
}
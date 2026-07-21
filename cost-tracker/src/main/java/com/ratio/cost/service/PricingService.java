package com.ratio.cost.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PricingService {
    
    // Pricing per 1000 tokens
    private static final Map<String, double[]> PRICING = new HashMap<>();
    
    static {
        // [input price per 1K, output price per 1K]
        PRICING.put("gpt-4o", new double[]{0.005, 0.015});
        PRICING.put("gpt-4o-mini", new double[]{0.00015, 0.0006});
        PRICING.put("gpt-3.5-turbo", new double[]{0.0005, 0.0015});
    }
    
    public double calculateCost(String model, int promptTokens, int completionTokens) {
        double[] prices = PRICING.getOrDefault(model, PRICING.get("gpt-4o"));
        
        double inputCost = (promptTokens / 1000.0) * prices[0];
        double outputCost = (completionTokens / 1000.0) * prices[1];
        
        return inputCost + outputCost;
    }
}
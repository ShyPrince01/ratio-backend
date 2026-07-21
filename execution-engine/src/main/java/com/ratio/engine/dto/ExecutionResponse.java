package com.ratio.engine.dto;

public class ExecutionResponse {
    private String output;
    private String model;
    private UsageInfo usage;
    
    public ExecutionResponse(String output, String model, UsageInfo usage) {
        this.output = output;
        this.model = model;
        this.usage = usage;
    }
    
    // Getters
    public String getOutput() { return output; }
    public String getModel() { return model; }
    public UsageInfo getUsage() { return usage; }
    
    public static class UsageInfo {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
        private double cost;
        
        public UsageInfo(int promptTokens, int completionTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = promptTokens + completionTokens;
        }
        
        // Getters
        public int getPromptTokens() { return promptTokens; }
        public int getCompletionTokens() { return completionTokens; }
        public int getTotalTokens() { return totalTokens; }
        public double getCost() { return cost; }
        public void setCost(double cost) { this.cost = cost; }
    }
}
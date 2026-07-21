package com.ratio.cost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class TrackRequest {
    
    private String promptId;
    
    @NotBlank
    private String model;
    
    @PositiveOrZero
    private int promptTokens;
    
    @PositiveOrZero
    private int completionTokens;
    
    private int totalTokens;
    
    // Getters and setters
    public String getPromptId() { return promptId; }
    public void setPromptId(String promptId) { this.promptId = promptId; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public int getPromptTokens() { return promptTokens; }
    public void setPromptTokens(int promptTokens) { this.promptTokens = promptTokens; }
    
    public int getCompletionTokens() { return completionTokens; }
    public void setCompletionTokens(int completionTokens) { this.completionTokens = completionTokens; }
    
    public int getTotalTokens() { return totalTokens; }
    public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }
}
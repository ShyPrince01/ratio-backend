package com.ratio.engine.dto;

public class ExecuteRequest {
    private String promptId;
    private String content; // Raw prompt content if no ID
    private String model = "gpt-4o";
    private Double temperature = 0.7;
    private Integer maxTokens = 500;
    private String variables; // JSON string of variable values
    
    // Getters and setters
    public String getPromptId() { return promptId; }
    public void setPromptId(String promptId) { this.promptId = promptId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    
    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
    
    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }
}
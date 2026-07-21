package com.ratio.prompt.dto;

import jakarta.validation.constraints.NotBlank;

public class PromptRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String variables;
    private String model = "gpt-4o";
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
}
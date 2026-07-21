package com.ratio.prompt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prompts")
public class Prompt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Prompt name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Prompt content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(columnDefinition = "TEXT")
    private String variables; // JSON array: ["customer_name", "product"]
    
    @Column(nullable = false)
    private String model = "gpt-4o"; // Default model
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Prompt() {}
    
    public Prompt(String name, String content, String variables, String model) {
        this.name = name;
        this.content = content;
        this.variables = variables;
        this.model = model;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
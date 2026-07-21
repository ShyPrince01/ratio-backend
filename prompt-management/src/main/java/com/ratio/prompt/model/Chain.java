package com.ratio.prompt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chains")
public class Chain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String promptIds; // JSON array: ["uuid1", "uuid2", "uuid3"]
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors, getters, setters
    public Chain() {}
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPromptIds() { return promptIds; }
    public void setPromptIds(String promptIds) { this.promptIds = promptIds; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
}
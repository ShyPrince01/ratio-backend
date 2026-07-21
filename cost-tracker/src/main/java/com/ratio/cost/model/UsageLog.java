package com.ratio.cost.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_logs")
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String promptId;

    @Column(nullable = false)
    private String model;

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;

    private double cost;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public UsageLog() {}

    public UsageLog(String promptId, String model, int promptTokens,
                   int completionTokens, int totalTokens, double cost) {
        this.promptId = promptId;
        this.model = model;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
        this.cost = cost;
    }

    // Getters and setters
    public Long getId() { return id; }
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
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
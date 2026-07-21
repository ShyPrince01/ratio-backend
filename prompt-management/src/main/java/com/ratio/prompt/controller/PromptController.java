package com.ratio.prompt.controller;

import com.ratio.prompt.dto.PromptRequest;
import com.ratio.prompt.model.Prompt;
import com.ratio.prompt.repository.PromptRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prompts")
@CrossOrigin(origins = "*") // For development; tighten in production
public class PromptController {
    
    private final PromptRepository promptRepository;
    
    public PromptController(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }
    
    // Create a new prompt
    @PostMapping
    public ResponseEntity<Prompt> createPrompt(@Valid @RequestBody PromptRequest request) {
        Prompt prompt = new Prompt(
            request.getName(),
            request.getContent(),
            request.getVariables(),
            request.getModel()
        );
        Prompt saved = promptRepository.save(prompt);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    // Get all prompts
    @GetMapping
    public ResponseEntity<List<Prompt>> getAllPrompts() {
        return ResponseEntity.ok(promptRepository.findAll());
    }
    
    // Get a specific prompt
    @GetMapping("/{id}")
    public ResponseEntity<Prompt> getPrompt(@PathVariable UUID id) {
        return promptRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Update a prompt
    @PutMapping("/{id}")
    public ResponseEntity<Prompt> updatePrompt(
            @PathVariable UUID id, 
            @Valid @RequestBody PromptRequest request) {
        return promptRepository.findById(id)
            .map(prompt -> {
                prompt.setName(request.getName());
                prompt.setContent(request.getContent());
                prompt.setVariables(request.getVariables());
                prompt.setModel(request.getModel());
                return ResponseEntity.ok(promptRepository.save(prompt));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Delete a prompt
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable UUID id) {
        if (promptRepository.existsById(id)) {
            promptRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
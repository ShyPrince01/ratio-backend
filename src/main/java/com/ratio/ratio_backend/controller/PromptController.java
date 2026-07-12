package com.ratio.ratio_backend.controller;


import com.ratio.ratio_backend.model.Prompt;
import com.ratio.ratio_backend.repository.PromptRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompts")
@CrossOrigin(origins = "*") // Allows your frontend to connect
public class PromptController {
    private final PromptRepository repository;

    public PromptController(PromptRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Prompt create(@RequestBody Prompt prompt) {
        return repository.save(prompt);
    }
}
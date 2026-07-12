package com.ratio.ratio_backend.repository;

import com.ratio.ratio_backend.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PromptRepository extends JpaRepository<Prompt, UUID> {
}
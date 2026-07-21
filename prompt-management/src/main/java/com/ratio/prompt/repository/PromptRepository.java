package com.ratio.prompt.repository;

import com.ratio.prompt.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, UUID> {
    // Spring Data JPA automatically implements CRUD operations
    // Additional query methods can be added here
}
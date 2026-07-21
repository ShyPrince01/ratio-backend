package com.ratio.prompt.repository;

import com.ratio.prompt.model.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChainRepository extends JpaRepository<Chain, UUID> {
}
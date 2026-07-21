package com.ratio.cost.repository;

import com.ratio.cost.model.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageLogRepository extends JpaRepository<UsageLog, Long> {

    List<UsageLog> findByPromptId(String promptId);

}
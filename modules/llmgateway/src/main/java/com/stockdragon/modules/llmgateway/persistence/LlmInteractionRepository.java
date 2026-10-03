package com.stockdragon.modules.llmgateway.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LlmInteractionRepository
        extends JpaRepository<LlmInteractionEntity, UUID> {
}
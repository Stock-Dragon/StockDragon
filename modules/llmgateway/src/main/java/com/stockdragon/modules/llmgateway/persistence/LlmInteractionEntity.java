package com.stockdragon.modules.llmgateway.persistence;

import com.stockdragon.libraries.jpacommons.BaseEntity;
import com.stockdragon.modules.llmgateway.domain.LlmProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "llm_interactions")
@Getter
@NoArgsConstructor
public class LlmInteractionEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LlmProviderType provider;

    @Column(nullable = false)
    private String model;

    @Column(columnDefinition = "TEXT")
    private String request;

    @Column(columnDefinition = "TEXT")
    private String response;

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private Double temperature;
    private Integer maxTokens;
    private String finishReason;
    private Long latencyMs;

    private String conversationId;
    private String analysisId;
    private String traceId;

    public LlmInteractionEntity(
            LlmProviderType provider,
            String model,
            String request,
            String response,
            Integer promptTokens,
            Integer completionTokens,
            Integer totalTokens,
            Double temperature,
            Integer maxTokens,
            String finishReason,
            Long latencyMs,
            String conversationId,
            String analysisId,
            String traceId
    ) {
        this.provider = provider;
        this.model = model;
        this.request = request;
        this.response = response;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.finishReason = finishReason;
        this.latencyMs = latencyMs;
        this.conversationId = conversationId;
        this.analysisId = analysisId;
        this.traceId = traceId;
    }
}
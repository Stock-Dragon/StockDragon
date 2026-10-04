package com.stockdragon.modules.llmgateway.api.model;

import com.stockdragon.modules.llmgateway.domain.LlmProviderType;

import java.util.List;
import java.util.Map;

public record LlmRequest(
        LlmProviderType provider,
        String model,
        List<LlmMessage> messages,
        Double temperature,
        Integer maxTokens,
        Map<String, Object> metadata,
        String conversationId,
        String analysisId,
        String traceId
) {
}
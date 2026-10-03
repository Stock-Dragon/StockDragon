package com.stockdragon.modules.llmgateway.api.model;

import com.stockdragon.modules.llmgateway.domain.LlmProviderType;

public record LlmResponse(
        String content,
        LlmProviderType provider,
        String model,
        LlmUsage usage,
        String finishReason,
        long latencyMs
) {
}
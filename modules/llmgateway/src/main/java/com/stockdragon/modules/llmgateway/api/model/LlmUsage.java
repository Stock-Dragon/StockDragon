package com.stockdragon.modules.llmgateway.api.model;

public record LlmUsage(
        Integer promptTokens,
        Integer completionTokens,
        Integer totalTokens
) {
}
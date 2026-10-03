package com.stockdragon.modules.llmgateway.api.model;

public record LlmMessage(
        Role role,
        String content
) {

    public enum Role {
        SYSTEM,
        USER,
        ASSISTANT
    }
}
package com.stockdragon.modules.llmgateway.domain;

import com.stockdragon.modules.llmgateway.api.model.LlmRequest;
import com.stockdragon.modules.llmgateway.api.model.LlmResponse;

public interface LlmProvider {

    LlmResponse chat(LlmRequest request);

    LlmProviderType getProviderType();
}
package com.stockdragon.modules.llmgateway.api;

import com.stockdragon.modules.llmgateway.api.model.LlmRequest;
import com.stockdragon.modules.llmgateway.api.model.LlmResponse;

public interface LlmGateway {

    LlmResponse chat(LlmRequest request);
}
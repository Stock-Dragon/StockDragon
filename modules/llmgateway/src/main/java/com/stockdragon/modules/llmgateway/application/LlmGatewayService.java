package com.stockdragon.modules.llmgateway.application;

import com.stockdragon.modules.llmgateway.api.LlmGateway;
import com.stockdragon.modules.llmgateway.api.model.LlmRequest;
import com.stockdragon.modules.llmgateway.api.model.LlmResponse;
import com.stockdragon.modules.llmgateway.domain.LlmProvider;
import com.stockdragon.modules.llmgateway.persistence.LlmInteractionEntity;
import com.stockdragon.modules.llmgateway.persistence.LlmInteractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LlmGatewayService implements LlmGateway {

    private final Map<?, LlmProvider> providers;
    private final LlmInteractionRepository interactionRepository;

    public LlmGatewayService(
            List<LlmProvider> providers,
            LlmInteractionRepository interactionRepository
    ) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(
                        LlmProvider::getProviderType,
                        Function.identity()
                ));

        this.interactionRepository = interactionRepository;
    }

    @Override
    public LlmResponse chat(LlmRequest request) {

        LlmProvider provider = providers.get(request.provider());

        if (provider == null) {
            throw new IllegalArgumentException(
                    "Unsupported LLM provider: " + request.provider()
            );
        }

        LlmResponse response = provider.chat(request);

        saveInteraction(request, response);

        return response;
    }

    private void saveInteraction(
            LlmRequest request,
            LlmResponse response
    ) {

        LlmInteractionEntity entity = new LlmInteractionEntity(
                request.provider(),
                response.model(),
                serializeMessages(request),
                response.content(),
                response.usage().promptTokens(),
                response.usage().completionTokens(),
                response.usage().totalTokens(),
                request.temperature(),
                request.maxTokens(),
                response.finishReason(),
                response.latencyMs(),
                request.conversationId(),
                request.analysisId(),
                request.traceId()
        );

        interactionRepository.save(entity);
    }

    private String serializeMessages(LlmRequest request) {
        return request.messages()
                .stream()
                .map(message ->
                        message.role() + ": " + message.content()
                )
                .collect(Collectors.joining("\n"));
    }
}
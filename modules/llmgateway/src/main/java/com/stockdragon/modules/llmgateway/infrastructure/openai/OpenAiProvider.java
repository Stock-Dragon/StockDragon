package com.stockdragon.modules.llmgateway.infrastructure.openai;

import com.stockdragon.modules.llmgateway.api.model.LlmMessage;
import com.stockdragon.modules.llmgateway.api.model.LlmRequest;
import com.stockdragon.modules.llmgateway.api.model.LlmResponse;
import com.stockdragon.modules.llmgateway.api.model.LlmUsage;
import com.stockdragon.modules.llmgateway.domain.LlmProvider;
import com.stockdragon.modules.llmgateway.domain.LlmProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiProvider implements LlmProvider {

    private final OpenAiChatModel chatModel;

    @Override
    public LlmResponse chat(LlmRequest request) {

        List<Message> messages = request.messages()
                .stream()
                .map(this::mapMessage)
                .toList();

        ChatOptions options = ChatOptions.builder()
                .model(request.model())
                .temperature(request.temperature())
                .maxTokens(request.maxTokens())
                .build();

        Prompt prompt = new Prompt(messages, options);

        long start = System.currentTimeMillis();

        ChatResponse response = chatModel.call(prompt);

        long latency = System.currentTimeMillis() - start;

        var result = response.getResult();

        var metadata = response.getMetadata();

        Integer promptTokens = Math.toIntExact(metadata.getUsage() != null
                ? metadata.getUsage().getPromptTokens()
                : null);

        Integer completionTokens = metadata.getUsage() != null
                ? metadata.getUsage().getCompletionTokens()
                : null;

        Integer totalTokens = metadata.getUsage() != null
                ? metadata.getUsage().getTotalTokens()
                : null;

        LlmUsage usage = new LlmUsage(
                promptTokens,
                completionTokens,
                totalTokens
        );

        return new LlmResponse(
                result.getOutput().getText(),
                LlmProviderType.OPENAI,
                request.model(),
                usage,
                result.getMetadata().getFinishReason(),
                latency
        );
    }

    private Message mapMessage(LlmMessage message) {
        return switch (message.role()) {
            case SYSTEM -> new SystemMessage(message.content());
            case USER -> new UserMessage(message.content());
            case ASSISTANT -> new AssistantMessage(message.content());
        };
    }

    @Override
    public LlmProviderType getProviderType() {
        return LlmProviderType.OPENAI;
    }
}
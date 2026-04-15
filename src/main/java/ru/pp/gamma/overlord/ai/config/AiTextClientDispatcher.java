package ru.pp.gamma.overlord.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfPathSmartAiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfPathStyleAiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfResponsesStyleAiTextClient;
import ru.pp.gamma.overlord.ai.gigachat.GigaChatAiTextClient;
import ru.pp.gamma.overlord.ai.model.AiModel;

@Component
@RequiredArgsConstructor
public class AiTextClientDispatcher implements AiTextClient {

    private final CfPathStyleAiTextClient cfPathClient;
    private final CfPathSmartAiTextClient cfPathSmartClient;
    private final CfResponsesStyleAiTextClient cfResponsesClient;
    private final GigaChatAiTextClient gigaChatClient;

    @Override
    public String generate(String systemPrompt, String userPrompt, AiModel model) {
        String modelId = model.getModelId();
        int maxTokens = model.getMaxOutputTokens();

        return switch (model.getApiStyle()) {
            case CF_PATH -> cfPathClient.generate(systemPrompt, userPrompt, modelId, maxTokens);
            case CF_PATH_SMART -> cfPathSmartClient.generate(systemPrompt, userPrompt, modelId, maxTokens);
            case CF_RESPONSES -> cfResponsesClient.generate(systemPrompt, userPrompt, modelId);
            case GIGACHAT -> gigaChatClient.generate(systemPrompt, userPrompt, modelId);
        };
    }
}
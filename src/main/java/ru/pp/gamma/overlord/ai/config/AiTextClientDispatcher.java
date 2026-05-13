package ru.pp.gamma.overlord.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfPathSmartAiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfResponsesStyleAiTextClient;
import ru.pp.gamma.overlord.ai.gigachat.GigaChatAiTextClient;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.ai.model.AiModelParam;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AiTextClientDispatcher implements AiTextClient {

    private final CfPathSmartAiTextClient cfPathSmartClient;
    private final CfResponsesStyleAiTextClient cfResponsesClient;
    private final GigaChatAiTextClient gigaChatClient;

    @Override
    public String generate(String systemPrompt, String userPrompt, AiModel model,
                           Map<AiModelParam, Object> modelParams) {
        Map<AiModelParam, Object> effectiveParams = merge(model.getDefaultParams(), modelParams);
        String modelId = model.getModelId();

        return switch (model.getApiStyle()) {
            case CF_PATH_SMART -> cfPathSmartClient.generate(systemPrompt, userPrompt, modelId, effectiveParams);
            case CF_RESPONSES -> cfResponsesClient.generate(systemPrompt, userPrompt, modelId);
            case GIGACHAT -> gigaChatClient.generate(systemPrompt, userPrompt, modelId);
        };
    }

    private Map<AiModelParam, Object> merge(Map<AiModelParam, Object> defaults,
                                            Map<AiModelParam, Object> overrides) {
        Map<AiModelParam, Object> merged = new HashMap<>(defaults);
        if (overrides != null) merged.putAll(overrides);
        return merged;
    }
}
package ru.pp.gamma.overlord.ai.api;

import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.ai.model.AiModelParam;

import java.util.Map;

public interface AiTextClient {
    String generate(String systemPrompt, String userPrompt, AiModel model,
                    Map<AiModelParam, Object> modelParams);
}

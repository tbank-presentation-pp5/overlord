package ru.pp.gamma.overlord.ai.api;

import ru.pp.gamma.overlord.ai.model.AiModel;

public interface AiTextClient {
    String generate(String systemPrompt, String userPrompt, AiModel model);
}

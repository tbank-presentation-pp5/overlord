package ru.pp.gamma.overlord.ai.api;

import ru.pp.gamma.overlord.ai.model.AiImageModel;

public interface AiImageClient {
    byte[] generate(String systemPrompt, String userPrompt, int height, int width, AiImageModel model);
}
package ru.pp.gamma.overlord.ai.api;

public interface AiImageClient {
    byte[] generate(String systemPrompt, String userPrompt, int height, int width);
}

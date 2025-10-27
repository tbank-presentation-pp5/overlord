package ru.pp.gamma.overlord.ai.api;

public interface AiTextClient {
    String generate(String systemPrompt, String userPrompt);
}

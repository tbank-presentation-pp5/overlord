package ru.pp.gamma.overlord.presentation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.generation.prompt.SystemPromptProvider;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromTextRequest;

@RequiredArgsConstructor
@Service
public class PresentationGenerationService {

    private final SystemPromptProvider systemPromptProvider;
    private final AiTextClient aiTextClient;

    public String generate(PresentationGenerationFromTextRequest request) {
        //todo: Сделать нормально, только для демонстрации

        var s = systemPromptProvider.getPrompt(1);
        var resp = aiTextClient.generate(s, request.text());
        return resp;
    }

}

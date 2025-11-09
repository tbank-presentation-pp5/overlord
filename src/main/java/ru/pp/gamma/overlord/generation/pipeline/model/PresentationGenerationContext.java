package ru.pp.gamma.overlord.generation.pipeline.model;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

@Getter
@Setter
public class PresentationGenerationContext {
    private Presentation presentation;
    private AiPresentationResponse response;
    private long templatePresentationId;
    private String userPrompt;
}

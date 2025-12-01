package ru.pp.gamma.overlord.generation.pipeline.model;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.model.PresentationGenerationSource;

@Getter
@Setter
public class PresentationGenerationContext {
    private Presentation presentation;
    private AiPresentationResponse response;
    private GenerationParams params;
}

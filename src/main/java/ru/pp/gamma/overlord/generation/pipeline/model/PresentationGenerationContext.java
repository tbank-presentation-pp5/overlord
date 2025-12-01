package ru.pp.gamma.overlord.generation.pipeline.model;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.generation.prompt.GenerationPrompt;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;

@Getter
@Setter
public class PresentationGenerationContext {
    private Presentation presentation;
    private AiPresentationResponse response;
    private TemplatePresentation template;
    private GenerationPrompt prompt;
}

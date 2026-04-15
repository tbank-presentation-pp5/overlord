package ru.pp.gamma.overlord.generation.pipeline.model;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.generation.prompt.GenerationPrompt;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;

@Getter
@Setter
public class PresentationGenerationContext {
    private AiModel aiModel;
    private AiImageModel aiImageModel;
    private GenerationPrompt prompt;
    private TemplatePresentation template;
    private Presentation presentation;
}
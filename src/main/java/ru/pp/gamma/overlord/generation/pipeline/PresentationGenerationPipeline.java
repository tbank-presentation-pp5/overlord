package ru.pp.gamma.overlord.generation.pipeline;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.generation.pipeline.step.PresentationGenerationStep;
import ru.pp.gamma.overlord.generation.prompt.GenerationPrompt;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;

import java.util.List;

@Component
public class PresentationGenerationPipeline {

    private final List<PresentationGenerationStep> steps;

    public PresentationGenerationPipeline(List<PresentationGenerationStep> steps) {
        this.steps = steps;
    }

    public Presentation generate(TemplatePresentation template, GenerationPrompt prompt) {
        PresentationGenerationContext context = new PresentationGenerationContext();
        context.setTemplate(template);
        context.setPrompt(prompt);

        for (PresentationGenerationStep step : steps) {
            step.process(context);
        }

        return context.getPresentation();
    }

}

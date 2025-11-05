package ru.pp.gamma.overlord.generation.pipeline;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.generation.pipeline.step.PresentationGenerationStep;

import java.util.List;

@Component
public class PresentationGenerationPipeline {

    private final List<PresentationGenerationStep> steps;

    public PresentationGenerationPipeline(List<PresentationGenerationStep> steps) {
        this.steps = steps;
    }

    public Presentation generate(int templatePresentationId, String userPrompt) {
        PresentationGenerationContext context = new PresentationGenerationContext();
        context.setTemplatePresentationId(templatePresentationId);
        context.setUserPrompt(userPrompt);


        for (PresentationGenerationStep step : steps) {
            step.process(context);
        }

        return context.getPresentation();
    }

}

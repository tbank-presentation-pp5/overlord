package ru.pp.gamma.overlord.generation.pipeline.step;

import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;

public interface PresentationGenerationStep {
    void process(PresentationGenerationContext context);
}

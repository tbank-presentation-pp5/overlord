package ru.pp.gamma.overlord.generation.pipeline.model;

import ru.pp.gamma.overlord.presentation.model.PresentationGenerationSource;

public record GenerationParams(
        long templatePresentationId,
        String userPrompt,
        PresentationGenerationSource generationSource,
        int slidesCount
) {
}

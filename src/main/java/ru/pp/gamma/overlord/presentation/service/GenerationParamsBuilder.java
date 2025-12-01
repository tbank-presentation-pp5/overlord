package ru.pp.gamma.overlord.presentation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.generation.pipeline.model.GenerationParams;
import ru.pp.gamma.overlord.generation.prompt.UserPromptBuilder;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromPlanRequest;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromTextRequest;
import ru.pp.gamma.overlord.presentation.model.PresentationGenerationSource;
import ru.pp.gamma.overlord.presentationplan.service.PresentationPlanService;

@RequiredArgsConstructor
@Component
public class GenerationParamsBuilder {

    private final UserPromptBuilder userPromptBuilder;
    private final PresentationPlanService presentationPlanService;

    public GenerationParams fromTextRequest(PresentationGenerationFromTextRequest dto) {
        return new GenerationParams(
                dto.templatePresentationId(),
                userPromptBuilder.buildNoteSource(dto.note(), dto.numberOfSlides()),
                PresentationGenerationSource.NOTE,
                dto.numberOfSlides()
        );
    }

    public GenerationParams fromPlanRequest(PresentationGenerationFromPlanRequest dto) {
        return new GenerationParams(
                dto.templatePresentationId(),
                userPromptBuilder.buildPlanPrompt(dto.planId()),
                PresentationGenerationSource.PLAN,
                presentationPlanService.getById(dto.planId()).getSlidesCount()
        );
    }

}

package ru.pp.gamma.overlord.presentation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.generation.pipeline.PresentationGenerationPipeline;
import ru.pp.gamma.overlord.generation.prompt.GenerationPrompt;
import ru.pp.gamma.overlord.generation.prompt.GenerationPromptBuilder;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentation.template.service.TemplatePresentationService;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.service.PresentationPlanService;

@RequiredArgsConstructor
@Service
public class PresentationGenerationService {

    private final PresentationGenerationPipeline presentationGenerationPipeline;
    private final PresentationRepository presentationRepository;
    private final GenerationPromptBuilder generationPromptBuilder;
    private final TemplatePresentationService templatePresentationService;
    private final PresentationPlanService presentationPlanService;

    public Presentation generateFromNote(String note, long templatePresentationId, int numberOfSlides) {
        TemplatePresentation template = templatePresentationService.getById(templatePresentationId);
        GenerationPrompt prompt = generationPromptBuilder.buildWithNoteSource(template, note, numberOfSlides);

        Presentation presentation = presentationGenerationPipeline.generate(template, prompt);
        presentationRepository.save(presentation);

        return presentation;
    }

    public Presentation generateFromPlan(long planId, long templatePresentationId) {
        TemplatePresentation template = templatePresentationService.getById(templatePresentationId);
        PresentationPlan plan = presentationPlanService.getById(planId);
        GenerationPrompt prompt = generationPromptBuilder.buildWithPlanSource(template, plan);

        Presentation presentation = presentationGenerationPipeline.generate(template, prompt);
        presentationRepository.save(presentation);

        return presentation;
    }
}

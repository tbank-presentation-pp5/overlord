package ru.pp.gamma.overlord.presentation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.generation.pipeline.PresentationGenerationPipeline;
import ru.pp.gamma.overlord.generation.pipeline.model.GenerationParams;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;

@RequiredArgsConstructor
@Service
public class PresentationGenerationService {

    private final PresentationGenerationPipeline presentationGenerationPipeline;
    private final PresentationRepository presentationRepository;

    public Presentation generate(GenerationParams params) {
        Presentation presentation = presentationGenerationPipeline.generate(params);
        presentationRepository.save(presentation);

        return presentation;
    }
}

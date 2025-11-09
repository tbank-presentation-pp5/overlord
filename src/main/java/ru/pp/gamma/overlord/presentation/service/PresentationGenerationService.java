package ru.pp.gamma.overlord.presentation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.generation.pipeline.PresentationGenerationPipeline;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromTextRequest;
import ru.pp.gamma.overlord.presentation.dto.PresentationResponse;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.mapper.ApiPresentationMapper;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;

@RequiredArgsConstructor
@Service
public class PresentationGenerationService {

    private final PresentationGenerationPipeline presentationGenerationPipeline;
    private final PresentationRepository presentationRepository;
    private final ApiPresentationMapper apiPresentationMapper;

    public PresentationResponse generateFromNote(PresentationGenerationFromTextRequest request) {
        Presentation presentation = presentationGenerationPipeline.generate(
                request.templatePresentationId(),
                request.note()
        );
        presentationRepository.save(presentation);

        return apiPresentationMapper.toResponse(presentation);
    }

}

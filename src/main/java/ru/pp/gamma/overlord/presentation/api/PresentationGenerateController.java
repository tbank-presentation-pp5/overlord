package ru.pp.gamma.overlord.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromPlanRequest;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromTextRequest;
import ru.pp.gamma.overlord.presentation.dto.PresentationResponse;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.mapper.ApiPresentationMapper;
import ru.pp.gamma.overlord.presentation.service.PresentationGenerationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/generate")
public class PresentationGenerateController {

    private final PresentationGenerationService presentationGenerationService;
    private final ApiPresentationMapper apiPresentationMapper;

    @PostMapping("/note")
    public PresentationResponse generateFromNote(@RequestBody PresentationGenerationFromTextRequest request) {
        Presentation presentation = presentationGenerationService
                .generateFromNote(request.note(), request.templatePresentationId(), request.numberOfSlides());
        return apiPresentationMapper.toResponse(presentation);
    }

    @PostMapping("/plan")
    public PresentationResponse generateFromPlan(@RequestBody PresentationGenerationFromPlanRequest request) {
        Presentation presentation = presentationGenerationService
                .generateFromPlan(request.planId(), request.templatePresentationId());
        return apiPresentationMapper.toResponse(presentation);
    }
}

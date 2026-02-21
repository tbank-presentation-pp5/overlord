package ru.pp.gamma.overlord.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.presentation.dto.PresentationResponse;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.mapper.ApiPresentationMapper;
import ru.pp.gamma.overlord.presentation.service.PresentationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations")
public class PresentationController {

    private final PresentationService presentationService;
    private final ApiPresentationMapper apiPresentationMapper;

    @GetMapping("/{id}")
    public PresentationResponse getById(@PathVariable long id) {
        Presentation presentation = presentationService.getByIdOrderedSlides(id);
        return apiPresentationMapper.toResponse(presentation);
    }

}

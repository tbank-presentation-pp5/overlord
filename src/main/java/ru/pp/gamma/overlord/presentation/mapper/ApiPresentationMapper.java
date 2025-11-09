package ru.pp.gamma.overlord.presentation.mapper;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentation.dto.PresentationResponse;
import ru.pp.gamma.overlord.presentation.dto.PresentationSlideResponse;
import ru.pp.gamma.overlord.presentation.dto.SlideContentResponse;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;

@Component
public class ApiPresentationMapper {

    public PresentationResponse toResponse(Presentation presentation) {
        return new PresentationResponse(
                presentation.getTemplate().getId(),
                presentation.getId(),
                presentation.getName(),
                presentation.getCreatedAt(),
                presentation.getSlides().stream().map(this::toResponse).toList()
        );
    }

    private PresentationSlideResponse toResponse(PresentationSlide slide) {
        return new PresentationSlideResponse(
                slide.getId(),
                slide.getTemplate().getId(),
                slide.getTemplate().getType(),
                slide.getOrderNumber(),
                slide.getFields().stream().map(this::toResponse).toList()
        );
    }

    private SlideContentResponse toResponse(SlideField field) {
        return new SlideContentResponse(
                field.getTemplate().getId(),
                field.getId(),
                field.getTemplate().getType(),
                field.getTemplate().getJsonKey(),
                field.getValue()
        );
    }

}

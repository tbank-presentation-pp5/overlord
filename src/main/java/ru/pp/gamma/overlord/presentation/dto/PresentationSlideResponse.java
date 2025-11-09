package ru.pp.gamma.overlord.presentation.dto;

import ru.pp.gamma.overlord.presentation.template.entity.SlideType;

import java.util.List;

public record PresentationSlideResponse(
        Long slideId,
        Long templateSlideId,
        SlideType type,
        Integer orderNumber,
        List<SlideContentResponse> content
) {
}

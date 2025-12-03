package ru.pp.gamma.overlord.presentationplan.dto;

import java.util.List;

public record PresentationPlanResponseDto(
        long id,
        String shortDescription,
        int numberOfSlides,
        List<PresentationElementDto> plan
) {
}

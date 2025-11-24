package ru.pp.gamma.overlord.presentationplan.dto;

import java.util.List;

public record GeneratePresentationPlanResponseDto(
        long id,
        String shortDescription,
        int numberOfSlides,
        List<GenerationPresentationElementDto> plan
) {
}

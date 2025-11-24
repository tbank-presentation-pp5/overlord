package ru.pp.gamma.overlord.presentationplan.dto;

public record GeneratePresentationPlanRequestDto(
        int numberOfSlides,
        String shortDescription
) {
}

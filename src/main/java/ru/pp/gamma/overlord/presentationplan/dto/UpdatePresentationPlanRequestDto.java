package ru.pp.gamma.overlord.presentationplan.dto;

import java.util.List;

public record UpdatePresentationPlanRequestDto(
        List<PresentationElementDto> plan
) {
}

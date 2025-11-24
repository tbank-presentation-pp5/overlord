package ru.pp.gamma.overlord.presentationplan.dto.ai;

import java.util.List;

public record AiPresentationPlanElementDto(
        String title,
        List<String> points
) {
}

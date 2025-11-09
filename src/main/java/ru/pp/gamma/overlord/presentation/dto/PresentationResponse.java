package ru.pp.gamma.overlord.presentation.dto;

import java.time.Instant;
import java.util.List;

public record PresentationResponse(
        Long templatePresentationId,
        Long presentationId,
        String name,
        Instant createdAt,
        List<PresentationSlideResponse> slides
) {
}

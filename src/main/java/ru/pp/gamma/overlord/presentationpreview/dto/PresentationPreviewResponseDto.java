package ru.pp.gamma.overlord.presentationpreview.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record PresentationPreviewResponseDto(
        long presentationId,
        String name,
        Instant updatedAt,
        List<String> previewUrls
) {
}

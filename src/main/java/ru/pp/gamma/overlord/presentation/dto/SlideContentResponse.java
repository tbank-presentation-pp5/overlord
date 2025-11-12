package ru.pp.gamma.overlord.presentation.dto;

import ru.pp.gamma.overlord.image.dto.ImageResponse;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldType;

public record SlideContentResponse(
        Long templateFieldId,
        Long fieldId,
        SlideFieldType type,
        String key,
        String value,
        ImageResponse image
) {
}

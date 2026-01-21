package ru.pp.gamma.overlord.presentation.dto;

import com.fasterxml.jackson.databind.JsonNode;
import ru.pp.gamma.overlord.image.dto.ImageResponse;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType;

public record SlideContentResponse(
        Long templateFieldId,
        Long fieldId,
        SlideFieldContentType type,
        String key,
        JsonNode value,
        ImageResponse image
) {
}

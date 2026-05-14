package ru.pp.gamma.overlord.ai.controller.dto;

public record AiImageModelDto(
        String enumName,
        String modelId,
        String displayName,
        String apiStyle
) {
}
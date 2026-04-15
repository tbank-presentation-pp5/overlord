package ru.pp.gamma.overlord.ai.controller.dto;

public record AiModelDto(
        String enumName,
        String modelId,
        String displayName,
        String apiStyle
) {
}
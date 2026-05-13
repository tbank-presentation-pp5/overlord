package ru.pp.gamma.overlord.ai.controller.dto;

import java.util.List;

public record AiModelDto(
        String enumName,
        String modelId,
        String displayName,
        String apiStyle,
        List<AiModelParamValueDto> params
) {
}
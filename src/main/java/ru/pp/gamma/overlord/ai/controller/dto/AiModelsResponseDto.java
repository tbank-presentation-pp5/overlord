package ru.pp.gamma.overlord.ai.controller.dto;

import java.util.List;

public record AiModelsResponseDto(
        List<AiModelDto> models,
        List<AiModelParamInfoDto> paramDefs
) {
}

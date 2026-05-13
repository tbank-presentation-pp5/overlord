package ru.pp.gamma.overlord.ai.controller.dto;

import java.util.List;

public record AiModelParamValueDto(
        String name,
        Object defaultValue,
        List<String> possibleValues
) {
}

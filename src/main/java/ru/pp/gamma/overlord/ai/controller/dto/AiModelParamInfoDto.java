package ru.pp.gamma.overlord.ai.controller.dto;

import java.util.List;

public record AiModelParamInfoDto(
        String name,
        String jsonKey,
        String description,
        String type,
        Double min,
        Double max,
        Object specDefault,
        List<String> possibleValues
) {
}
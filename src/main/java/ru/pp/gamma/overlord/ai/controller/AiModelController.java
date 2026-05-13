package ru.pp.gamma.overlord.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.ai.controller.dto.AiModelDto;
import ru.pp.gamma.overlord.ai.controller.dto.AiModelParamInfoDto;
import ru.pp.gamma.overlord.ai.controller.dto.AiModelParamValueDto;
import ru.pp.gamma.overlord.ai.controller.dto.AiModelsResponseDto;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.ai.model.AiModelParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/models")
public class AiModelController {

    private static final List<AiModelParamInfoDto> PARAM_DEFS =
            Arrays.stream(AiModelParam.values())
                    .map(AiModelController::toParamDef)
                    .toList();

    @GetMapping
    public AiModelsResponseDto getAll() {
        List<AiModelDto> models = Arrays.stream(AiModel.values())
                .map(this::toDto)
                .toList();
        return new AiModelsResponseDto(models, PARAM_DEFS);
    }

    private AiModelDto toDto(AiModel model) {
        Map<AiModelParam, Object> modelDefaults = model.getDefaultParams();

        List<AiModelParamValueDto> params = modelDefaults.entrySet().stream()
                .map(e -> toParamValue(e.getKey(), e.getValue()))
                .toList();

        return new AiModelDto(
                model.name(),
                model.getModelId(),
                model.getDisplayName(),
                model.getApiStyle().name(),
                params
        );
    }

    private static AiModelParamValueDto toParamValue(AiModelParam param, Object modelDefault) {
        return new AiModelParamValueDto(
                param.name(),
                modelDefault,
                param.getPossibleValues()
        );
    }

    private static AiModelParamInfoDto toParamDef(AiModelParam param) {
        return new AiModelParamInfoDto(
                param.name(),
                param.getJsonKey(),
                param.getDescription(),
                param.getType().name(),
                param.getMin(),
                param.getMax(),
                param.getDefaultValue(),
                param.getPossibleValues()
        );
    }
}

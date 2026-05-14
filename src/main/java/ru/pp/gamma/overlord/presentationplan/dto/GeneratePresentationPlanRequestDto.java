package ru.pp.gamma.overlord.presentationplan.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.ai.model.AiModelParam;

import java.util.Map;

public record GeneratePresentationPlanRequestDto(
        String shortDescription,
        int numberOfSlides,

        @JsonSetter(nulls = Nulls.SKIP)
        AiModel model,

        @JsonSetter(nulls = Nulls.SKIP)
        Map<AiModelParam, Object> modelParams
) {
    public GeneratePresentationPlanRequestDto {
        if (model == null) {
            model = AiModel.CF_MISTRAL_SMALL;
        }
    }
}

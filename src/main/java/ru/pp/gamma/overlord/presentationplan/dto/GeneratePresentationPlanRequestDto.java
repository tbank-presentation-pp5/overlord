package ru.pp.gamma.overlord.presentationplan.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import ru.pp.gamma.overlord.ai.model.AiModel;

public record GeneratePresentationPlanRequestDto(
        String shortDescription,
        int numberOfSlides,

        @JsonSetter(nulls = Nulls.SKIP)
        AiModel model
) {
    public GeneratePresentationPlanRequestDto {
        if (model == null) {
            model = AiModel.CF_MISTRAL_SMALL;
        }
    }
}

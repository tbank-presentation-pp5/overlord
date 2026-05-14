package ru.pp.gamma.overlord.presentation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.ai.model.AiModelParam;

import java.util.Map;

public record PresentationGenerationFromTextRequest(
        String note,
        long templatePresentationId,
        int numberOfSlides,

        @JsonSetter(nulls = Nulls.SKIP)
        AiModel textModel,

        @JsonSetter(nulls = Nulls.SKIP)
        AiImageModel imageModel,

        @JsonSetter(nulls = Nulls.SKIP)
        Map<AiModelParam, Object> textModelParams
) {
    public PresentationGenerationFromTextRequest {
        if (textModel == null) textModel = AiModel.CF_MISTRAL_SMALL;
        if (imageModel == null) imageModel = AiImageModel.CF_FLUX_1_SCHNELL;
    }
}
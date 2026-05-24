package ru.pp.gamma.overlord.ai.controller.dto;

import jakarta.validation.constraints.NotBlank;
import ru.pp.gamma.overlord.ai.model.AiImageModel;

public record AiImageGenerateRequest(
        @NotBlank String prompt,
        AiImageModel model,
        Integer width,
        Integer height
) {
}

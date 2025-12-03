package ru.pp.gamma.overlord.ai.cf.text.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CfTextRequestDto(
        String model,
        List<CfTextMessageElement> input,
        CfReasoning reasoning,
        @JsonProperty("max_tokens") int maxTokens,
        @JsonProperty("max_output_tokens") int maxOutputTokens,
        boolean stream
) {
}

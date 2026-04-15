package ru.pp.gamma.overlord.ai.cf.text.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CfTextRequestDto(
        String model,
        String instructions,
        String input,
        CfReasoning reasoning,
        @JsonProperty("max_tokens") int maxTokens,
        @JsonProperty("max_output_tokens") int maxOutputTokens,
        boolean stream
) {
}

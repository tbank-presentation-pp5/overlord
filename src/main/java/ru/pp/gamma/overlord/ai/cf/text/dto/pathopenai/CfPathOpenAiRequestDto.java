package ru.pp.gamma.overlord.ai.cf.text.dto.pathopenai;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CfPathOpenAiRequestDto(
        List<CfPathOpenAiInputMessageDto> messages,

        @JsonProperty("max_tokens")
        int maxTokens,

        @JsonProperty("raw")
        boolean raw
) {
}
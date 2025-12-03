package ru.pp.gamma.overlord.ai.cf.text.dto.path;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CfTextRequestDto(
        List<CfTextMessageElement> messages,
        @JsonProperty("max_tokens") int maxTokens,
        boolean raw
) {
}

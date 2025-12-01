package ru.pp.gamma.overlord.ai.cf.text.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CfUsage(
        @JsonProperty("input_tokens") int inputTokens,
        @JsonProperty("output_tokens") int outputTokens,
        @JsonProperty("total_tokens") int totalTokens
) {}

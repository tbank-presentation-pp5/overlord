package ru.pp.gamma.overlord.ai.cf.text.dto.pathopenai;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.List;
import java.util.Map;

public record CfPathOpenAiRequestDto(
        List<CfPathOpenAiInputMessageDto> messages,

        @JsonAnyGetter
        Map<String, Object> extraParams
) {
}
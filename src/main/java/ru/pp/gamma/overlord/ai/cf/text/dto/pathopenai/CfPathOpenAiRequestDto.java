package ru.pp.gamma.overlord.ai.cf.text.dto.pathopenai;

import java.util.List;

public record CfPathOpenAiRequestDto(List<CfPathOpenAiInputMessageDto> messages) {
}
package ru.pp.gamma.overlord.ai.cf.text.dto.response;

import java.util.List;

public record CfTextResponseDto(
        String id,
        String model,
        List<CfOutput> output,
        String status
) {
}

package ru.pp.gamma.overlord.ai.cf.text.dto.response;

import java.util.List;

public record CfOutput(
        String id,
        List<String> summary,
        String type,
        List<CfContent> content,
        String status,
        CfTextRole role
) {
}

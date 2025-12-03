package ru.pp.gamma.overlord.ai.cf.text.dto.response;

public record CfTextMessageElement(
        CfTextRole role,
        String content
) {
}

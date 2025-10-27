package ru.pp.gamma.overlord.ai.cf.text.dto;

public record CfTextMessageElement(
        CfTextRole role,
        String content
) {
}

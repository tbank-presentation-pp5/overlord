package ru.pp.gamma.overlord.ai.cf.image.dto;

public record CfImageRequestDto(
        String prompt,
        int height,
        int width
) {
}

package ru.pp.gamma.overlord.ai.cf.image.dto;

public record CfImageResultResponseDto(
        CfImageBase64ResponseDto result,
        boolean success
) {
}

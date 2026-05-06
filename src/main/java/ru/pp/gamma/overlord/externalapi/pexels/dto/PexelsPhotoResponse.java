package ru.pp.gamma.overlord.externalapi.pexels.dto;

import lombok.Builder;

@Builder
public record PexelsPhotoResponse(
        long id,
        int width,
        int height,
        String url,
        String photographer,
        String photographerUrl,
        String alt
) {
}

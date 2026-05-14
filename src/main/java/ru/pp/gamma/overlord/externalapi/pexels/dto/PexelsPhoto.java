package ru.pp.gamma.overlord.externalapi.pexels.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PexelsPhoto(
        long id,
        int width,
        int height,
        String url,
        String photographer,
        @JsonProperty("photographer_url") String photographerUrl,
        @JsonProperty("photographer_id") long photographerId,
        @JsonProperty("avg_color") String avgColor,
        Src src,
        boolean liked,
        String alt
) {
    public record Src(
            String original,
            String large2x,
            String large,
            String medium,
            String small,
            String portrait,
            String landscape,
            String tiny
    ) {
    }
}

package ru.pp.gamma.overlord.ai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiImageModel {
    CF_FLUX_2_KLEIN_9B(
            "@cf/black-forest-labs/flux-2-klein-9b",
            AiImageApiStyle.CF_MULTIPART_BINARY,
            "FLUX.2 Klein 9B (Cloudflare)"
    ),
    CF_FLUX_2_KLEIN_4B(
            "@cf/black-forest-labs/flux-2-klein-4b",
            AiImageApiStyle.CF_MULTIPART_BINARY,
            "FLUX.2 Klein 4B (Cloudflare)"
    ),
    CF_FLUX_1_SCHNELL(
            "@cf/black-forest-labs/flux-1-schnell",
            AiImageApiStyle.CF_BASE64,
            "FLUX.1 Schnell (Cloudflare)"
    ),
    CF_PHOENIX_1_0(
            "@cf/leonardo/phoenix-1.0",
            AiImageApiStyle.CF_BINARY,
            "Phoenix 1.0 (Cloudflare)"
    ),
    CF_LUCID_ORIGIN(
            "@cf/leonardo/lucid-origin",
            AiImageApiStyle.CF_BASE64,
            "Lucid Origin (Cloudflare)"
    ),
    CF_FLUX_2_DEV(
            "@cf/black-forest-labs/flux-2-dev",
            AiImageApiStyle.CF_MULTIPART_BINARY,
            "FLUX.2 Dev (Cloudflare)"
    );


    private final String modelId;
    private final AiImageApiStyle apiStyle;
    private final String displayName;
}
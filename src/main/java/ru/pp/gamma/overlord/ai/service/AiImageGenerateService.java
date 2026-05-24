package ru.pp.gamma.overlord.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.enums.ImageFormat;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.image.service.NsfwPlaceholderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiImageGenerateService {

    private static final String SYSTEM_PROMPT =
            "A beautiful digital painting, illustration style, full frame composition, "
                    + "crisp details, for a professional presentation.";
    private static final AiImageModel DEFAULT_MODEL = AiImageModel.CF_FLUX_1_SCHNELL;
    private static final int DEFAULT_WIDTH = 704;
    private static final int DEFAULT_HEIGHT = 528;

    private final AiImageClient aiImageClient;
    private final ImageService imageService;
    private final NsfwPlaceholderService nsfwPlaceholderService;

    public String generate(String prompt, AiImageModel model, Integer width, Integer height) {
        AiImageModel resolvedModel = model != null ? model : DEFAULT_MODEL;
        int resolvedWidth = normalizeImageDimension(width != null ? width : DEFAULT_WIDTH);
        int resolvedHeight = normalizeImageDimension(height != null ? height : DEFAULT_HEIGHT);

        log.debug("Generating image: model={}, size={}x{}", resolvedModel.name(), resolvedWidth, resolvedHeight);

        try {
            byte[] imageBytes = aiImageClient.generate(SYSTEM_PROMPT, prompt, resolvedHeight, resolvedWidth, resolvedModel);
            Image image = imageService.uploadImage(imageBytes, ImageFormat.JPEG);
            return imageService.generateUrlByName(image.getName());
        } catch (RestClientResponseException e) {
            if (isNsfwError(e)) {
                log.warn("NSFW content detected for prompt '{}', returning placeholder.", prompt);
                return nsfwPlaceholderService.getUrl();
            }
            throw e;
        }
    }

    private static int normalizeImageDimension(int value) {
        int clamped = Math.max(256, Math.min(2048, value));
        int rounded = (clamped / 8) * 8;
        if (rounded != value) {
            log.warn("Image dimension {} normalized to {}", value, rounded);
        }
        return rounded;
    }

    private boolean isNsfwError(RestClientResponseException e) {
        if (e.getStatusCode().value() != 400) return false;
        String body = e.getResponseBodyAsString();
        return body.contains("\"code\":3030")
                || body.contains("NSFW content")
                || body.contains("has been flagged");
    }
}

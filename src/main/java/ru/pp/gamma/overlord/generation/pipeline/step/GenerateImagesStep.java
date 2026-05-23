package ru.pp.gamma.overlord.generation.pipeline.step;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientResponseException;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.ai.model.AiModelParam;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.enums.ImageFormat;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GenerateImagesStep implements PresentationGenerationStep {

    private static final String SYSTEM_PROMPT =
            "A beautiful digital painting, illustration style, full frame composition, "
                    + "crisp details, for a professional presentation.";

    private static final String NSFW_FIX_SYSTEM_PROMPT =
            "You are an expert at rewriting image generation prompts for safe content filters. "
                    + "You will receive the original prompt and all previous rewrite attempts that were "
                    + "still blocked as NSFW. Analyze all of them and produce the single best rewrite: "
                    + "maximally preserve the original visual concept and artistic intent, "
                    + "but make it fully safe (no NSFW, violence, or inappropriate content). "
                    + "Return ONLY the final prompt text, nothing else.";

    private static final AiImageModel DEFAULT_IMAGE_MODEL = AiImageModel.CF_FLUX_1_SCHNELL;
    private static final int MAX_RETRIES = 4;
    
    private static final AiModel NSFW_FIX_MODEL = AiModel.CF_GEMMA_4_26B_A4B_IT;
    private static final Map<AiModelParam, Object> NSFW_FIX_MODEL_PARAMS = Map.of(
            AiModelParam.MAX_TOKENS, 256_000,
            AiModelParam.MAX_COMPLETION_TOKENS, 1000,
            AiModelParam.TEMPERATURE, 0,
            AiModelParam.TOP_K, 64,
            AiModelParam.TOP_P, 0.95,
            AiModelParam.PRESENCE_PENALTY, 0.0,
            AiModelParam.FREQUENCY_PENALTY, 0.0,
            AiModelParam.RAW, true,
            AiModelParam.REASONING_EFFORT, "medium"
    );

    private final AiImageClient aiImageClient;
    private final AiTextClient aiTextClient;
    private final ImageService imageService;

    @Override
    public void process(PresentationGenerationContext context) {
        AiImageModel model = context.getAiImageModel() != null
                ? context.getAiImageModel()
                : DEFAULT_IMAGE_MODEL;

        getFieldsWithImageType(context.getPresentation())
                .forEach(field -> processField(field, model));
    }

    private List<SlideField> getFieldsWithImageType(Presentation presentation) {
        return presentation.getSlides().stream()
                .flatMap(slide -> slide.getFields().stream())
                .filter(field -> field.getTemplate().getContentType().equals(SlideFieldContentType.IMAGE))
                .toList();
    }

    private void processField(SlideField field, AiImageModel model) {
        JsonNode imageSize = field.getTemplate().getMeta().get("imageSize");
        int height = imageSize.get("height").asInt();
        int width = imageSize.get("width").asInt();

        String prompt = field.getValue().asText();

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                byte[] imageBytes = aiImageClient.generate(SYSTEM_PROMPT, prompt, height, width, model);
                String name = imageService.save(imageBytes, ImageFormat.JPEG);

                Image image = new Image();
                image.setName(name);
                field.setImage(image);
                return;
            } catch (RestClientResponseException e) {
                if (!isNsfwError(e) || attempt == MAX_RETRIES - 1) {
                    throw new RuntimeException(
                            "Image generation failed after %d attempt(s)".formatted(attempt + 1), e);
                }
                log.warn("NSFW error on attempt {}/{}, rephrasing prompt via text model: {}", attempt + 1, MAX_RETRIES, prompt);
                prompt = rephraseNsfwPrompt(prompt);
            }
        }
    }

    private boolean isNsfwError(RestClientResponseException e) {
        return e.getStatusCode().value() == 400
                && e.getResponseBodyAsString().contains("NSFW content");
    }

    private String rephraseNsfwPrompt(String prompt) {
        String userPrompt = "Rewrite this image generation prompt to remove NSFW content "
                + "while keeping the original meaning:\n\n" + prompt;
        return aiTextClient.generate(
                NSFW_FIX_SYSTEM_PROMPT,
                userPrompt,
                NSFW_FIX_MODEL,
                NSFW_FIX_MODEL_PARAMS
        );
    }
}

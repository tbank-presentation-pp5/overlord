package ru.pp.gamma.overlord.generation.pipeline.step;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientResponseException;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.enums.ImageFormat;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.image.service.NsfwPlaceholderService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GenerateImagesStep implements PresentationGenerationStep {

    private static final String SYSTEM_PROMPT =
            "A beautiful digital painting, illustration style, full frame composition, "
                    + "crisp details, for a professional presentation.";
    private static final AiImageModel DEFAULT_MODEL = AiImageModel.CF_FLUX_1_SCHNELL;

    private final AiImageClient aiImageClient;
    private final ImageService imageService;
    private final NsfwPlaceholderService nsfwPlaceholderService;

    @Override
    public void process(PresentationGenerationContext context) {
        AiImageModel model = context.getAiImageModel() != null
                ? context.getAiImageModel()
                : DEFAULT_MODEL;

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
        try {
            byte[] imageBytes = aiImageClient.generate(
                    SYSTEM_PROMPT,
                    field.getValue().asText(),
                    imageSize.get("height").asInt(),
                    imageSize.get("width").asInt(),
                    model
            );
            String name = imageService.save(imageBytes, ImageFormat.JPEG);
            Image image = new Image();
            image.setName(name);
            field.setImage(image);
        } catch (RestClientResponseException e) {
            if (isNsfwError(e)) {
                log.warn("NSFW content detected for prompt '{}', using placeholder.", field.getValue().asText());
                field.setImage(nsfwPlaceholderService.getPlaceholderImage());
            } else {
                throw e;
            }
        }
    }

    private boolean isNsfwError(RestClientResponseException e) {
        if (e.getStatusCode().value() != 400) return false;
        String body = e.getResponseBodyAsString();
        return body.contains("\"code\":3030")
                || body.contains("NSFW content")
                || body.contains("has been flagged");
    }
}

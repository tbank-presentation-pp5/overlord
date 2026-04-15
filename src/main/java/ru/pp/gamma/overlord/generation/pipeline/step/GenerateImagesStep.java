package ru.pp.gamma.overlord.generation.pipeline.step;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType;

import java.util.List;

@RequiredArgsConstructor
public class GenerateImagesStep implements PresentationGenerationStep {

    // Тестирую детект на NSFW фильтр вроде бы стало лучшее, ещё посмотрю
    //    private static final String SYSTEM_PROMPT =
//            "High-quality digital art, concept art style, detailed, vibrant colors, cinematic lighting, " +
//                    "sharp focus, clean lines, not a photo, hand-drawn look, edge-to-edge, full bleed, " +
//                    "absolutely no borders, no frames, no black bars, no letterbox, do not generate any black margins";
    private static final String SYSTEM_PROMPT = "A beautiful digital painting, illustration style, full frame composition, crisp details, for a professional presentation.";

    private static final AiImageModel DEFAULT_MODEL = AiImageModel.CF_FLUX_1_SCHNELL;

    private final AiImageClient aiImageClient;
    private final ImageService imageService;

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
        byte[] imageBytes = aiImageClient.generate(
                SYSTEM_PROMPT,
                field.getValue().asText(),
                imageSize.get("height").asInt(),
                imageSize.get("width").asInt(),
                model
        );

        String name = imageService.save(imageBytes);

        Image image = new Image();
        image.setName(name);
        field.setImage(image);
    }
}
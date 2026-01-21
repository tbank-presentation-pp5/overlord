package ru.pp.gamma.overlord.generation.pipeline.step;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType;

import java.util.List;

@RequiredArgsConstructor
public class GenerateImagesStep implements PresentationGenerationStep {

    private static final String SYSTEM_PROMPT = "High-quality digital art, concept art style, detailed, vibrant colors, cinematic lighting, sharp focus, clean lines, not a photo, hand-drawn look";

    private final AiImageClient aiImageClient;
    private final ImageService imageService;

    @Override
    public void process(PresentationGenerationContext context) {
        List<SlideField> fieldsToProcess = getFieldsWithImageType(context.getPresentation());
        fieldsToProcess.forEach(this::processField);
    }

    private List<SlideField> getFieldsWithImageType(Presentation presentation) {
        return presentation.getSlides().stream()
                .flatMap(slide -> slide.getFields().stream())
                .filter(field -> field.getTemplate().getContentType().equals(SlideFieldContentType.IMAGE))
                .toList();
    }

    private void processField(SlideField field) {
        JsonNode templateImage = field.getTemplate().getMeta().get("imageSize");
        byte[] imageBytes = aiImageClient.generate(
                SYSTEM_PROMPT,
                field.getValue().get("prompt").asText(),
                templateImage.get("height").asInt(),
                templateImage.get("width").asInt()
        );
        String name = imageService.save(imageBytes);

        Image image = new Image();
        image.setName(name);

        field.setImage(image);
    }
}

package ru.pp.gamma.overlord.generation.pipeline.step;

import lombok.RequiredArgsConstructor;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideFieldType;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateImage;

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
                .filter(field -> field.getTemplate().getType().equals(SlideFieldType.IMAGE))
                .toList();
    }

    private void processField(SlideField field) {
        TemplateImage templateImage = field.getTemplate().getTemplateImage();
        byte[] imageBytes = aiImageClient.generate(
                SYSTEM_PROMPT,
                field.getValue(),
                templateImage.getHeight(),
                templateImage.getWidth()
        );
        String name = imageService.save(imageBytes);

        Image image = new Image();
        image.setName(name);

        field.setImage(image);
    }
}

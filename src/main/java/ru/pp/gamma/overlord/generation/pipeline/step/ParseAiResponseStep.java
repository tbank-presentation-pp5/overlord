package ru.pp.gamma.overlord.generation.pipeline.step;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.generation.pipeline.model.AiPresentationResponse;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.generation.prompt.SystemPromptProvider;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideType;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlide;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlideField;
import ru.pp.gamma.overlord.presentation.template.service.TemplatePresentationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.pp.gamma.overlord.presentation.template.common.KeyConsts.SLIDE_TYPE;

@RequiredArgsConstructor
public class ParseAiResponseStep implements PresentationGenerationStep {

    private final SystemPromptProvider systemPromptProvider;
    private final AiTextClient aiTextClient;
    private final ObjectMapper objectMapper;
    private final TemplatePresentationService templatePresentationService;

    @Override
    public void process(PresentationGenerationContext context) {
        AiPresentationResponse aiResponse = getParsedResponse(context);
        TemplatePresentation templatePresentation = templatePresentationService
                .getById(context.getParams().templatePresentationId());

        Presentation presentation = createFullPresentation(templatePresentation, aiResponse);
        context.setPresentation(presentation);
    }

    private AiPresentationResponse getParsedResponse(PresentationGenerationContext context) {
        String systemPrompt = systemPromptProvider.getPrompt(context.getParams());
        String aiResponse = aiTextClient.generate(systemPrompt, context.getParams().userPrompt());

        try {
            return objectMapper.readValue(aiResponse, AiPresentationResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Presentation createFullPresentation(TemplatePresentation templatePresentation,
                                                AiPresentationResponse aiResponse) {
        Presentation presentation = new Presentation();
        presentation.setName(aiResponse.name());
        presentation.setTemplate(templatePresentation);

        List<PresentationSlide> slides = new ArrayList<>();
        AtomicInteger order = new AtomicInteger();
        aiResponse.slides()
                .forEach(fields -> slides.add(createSlide(presentation, templatePresentation, fields, order.incrementAndGet())));

        presentation.setSlides(slides);

        return presentation;
    }

    private PresentationSlide createSlide(
            Presentation presentation,
            TemplatePresentation templatePresentation,
            JsonNode fields,
            int order
    ) {
        SlideType slideType = SlideType.valueOf(fields.get(SLIDE_TYPE).asText());
        TemplateSlide templateSlide = templatePresentation.getSlides().stream()
                .filter(tempSlide -> tempSlide.getType().equals(slideType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can not fin template slide"));

        PresentationSlide slide = new PresentationSlide();
        slide.setPresentation(presentation);
        slide.setTemplate(templateSlide);
        slide.setOrderNumber(order);

        List<SlideField> slideFields = new ArrayList<>();
        templateSlide.getFields()
                .forEach(templateField ->
                        slideFields.add(createSlideField(
                                        slide,
                                        templateField,
                                        fields.get(templateField.getJsonKey()).asText()
                                )
                        )
                );

        slide.setFields(slideFields);

        return slide;
    }

    private SlideField createSlideField(
            PresentationSlide presentationSlide,
            TemplateSlideField templateSlideField,
            String value
    ) {
        SlideField slideField = new SlideField();
        slideField.setTemplate(templateSlideField);
        slideField.setSlide(presentationSlide);
        slideField.setValue(value);

        return slideField;
    }
}

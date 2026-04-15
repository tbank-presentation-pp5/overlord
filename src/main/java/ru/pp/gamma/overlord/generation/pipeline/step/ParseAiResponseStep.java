package ru.pp.gamma.overlord.generation.pipeline.step;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.generation.pipeline.model.AiPresentationResponse;
import ru.pp.gamma.overlord.generation.pipeline.model.PresentationGenerationContext;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.SlideType;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlide;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlideField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static ru.pp.gamma.overlord.presentation.template.common.KeyConsts.SLIDE_TYPE;

@Slf4j
@RequiredArgsConstructor
public class ParseAiResponseStep implements PresentationGenerationStep {

    private static final AiModel DEFAULT_MODEL = AiModel.CF_MISTRAL_SMALL;

    private static final Pattern ESCAPED_OBJECT_SEPARATOR =
            Pattern.compile("}\\s*]\\s*,\\s*\"\\{\"");

    private final AiTextClient aiTextClient;
    private final ObjectMapper objectMapper;

    @Override
    public void process(PresentationGenerationContext context) {
        AiPresentationResponse aiResponse = getParsedResponse(context);
        Presentation presentation = createFullPresentation(context.getTemplate(), aiResponse);
        context.setPresentation(presentation);
    }

    private AiPresentationResponse getParsedResponse(PresentationGenerationContext context) {
        AiModel model = context.getAiModel() != null ? context.getAiModel() : DEFAULT_MODEL;
        String rawResponse = aiTextClient.generate(
                context.getPrompt().systemPrompt(),
                context.getPrompt().userPrompt(),
                model
        );

        String json = extractJson(rawResponse, model);
        json = repairJson(json, model);

        try {
            return objectMapper.readValue(json, AiPresentationResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "Failed to parse AI presentation response from model [%s]. Raw: %s"
                            .formatted(model.getModelId(), rawResponse), e);
        }
    }

    private String extractJson(String raw, AiModel model) {
        if (raw == null || raw.isBlank()) {
            throw new RuntimeException("Empty response from model: " + model.getModelId());
        }

        int arrayStart = raw.indexOf('[');
        int objectStart = raw.indexOf('{');

        int start;
        if (arrayStart == -1 && objectStart == -1) {
            throw new RuntimeException(
                    "No JSON found in response from model [%s]. Raw: %s"
                            .formatted(model.getModelId(), raw));
        } else if (arrayStart == -1) {
            start = objectStart;
        } else if (objectStart == -1) {
            start = arrayStart;
        } else {
            start = Math.min(arrayStart, objectStart);
        }

        String json = raw.substring(start).stripTrailing();

        if (json.endsWith("```")) {
            json = json.substring(0, json.lastIndexOf("```")).stripTrailing();
        }

        if (start > 0) {
            log.warn("Model [{}]: stripped {} chars of non-JSON prefix.", model.getModelId(), start);
        }

        return json;
    }

    private String repairJson(String json, AiModel model) {
        if (ESCAPED_OBJECT_SEPARATOR.matcher(json).find()) {
            log.warn("Model [{}]: repairing escaped object separators in JSON.", model.getModelId());
            json = json
                    .replaceAll("}\\s*]\\s*,\\s*\"\\{\"", "},{\"")
                    .replaceAll("}\\s*,\\s*\"\\{\"", "},{\"");
            json = balanceBrackets(json);
        }
        return json;
    }

    private String balanceBrackets(String json) {
        int depth = 0;
        for (char c : json.toCharArray()) {
            if (c == '[') depth++;
            else if (c == ']') depth--;
        }
        if (depth > 0) {
            json = json + "]".repeat(depth);
        }
        return json;
    }

    private Presentation createFullPresentation(TemplatePresentation templatePresentation,
                                                AiPresentationResponse aiResponse) {
        Presentation presentation = new Presentation();
        presentation.setName(aiResponse.name());
        presentation.setTemplate(templatePresentation);

        List<PresentationSlide> slides = new ArrayList<>();
        AtomicInteger order = new AtomicInteger();
        aiResponse.slides()
                .forEach(fields -> slides.add(
                        createSlide(presentation, templatePresentation, fields, order.incrementAndGet())
                ));

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
                .filter(s -> s.getType().equals(slideType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find template slide for type: " + slideType));

        PresentationSlide slide = new PresentationSlide();
        slide.setPresentation(presentation);
        slide.setTemplate(templateSlide);
        slide.setOrderNumber(order);

        List<SlideField> slideFields = new ArrayList<>();
        templateSlide.getFields()
                .forEach(templateField ->
                        slideFields.add(createSlideField(slide, templateField,
                                fields.get(templateField.getSchemaKey())))
                );

        slide.setFields(slideFields);
        return slide;
    }

    private SlideField createSlideField(
            PresentationSlide presentationSlide,
            TemplateSlideField templateSlideField,
            JsonNode value
    ) {
        SlideField slideField = new SlideField();
        slideField.setTemplate(templateSlideField);
        slideField.setSlide(presentationSlide);
        slideField.setValue(value);
        return slideField;
    }
}
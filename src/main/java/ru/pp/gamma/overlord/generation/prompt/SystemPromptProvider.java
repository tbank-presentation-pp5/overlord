package ru.pp.gamma.overlord.generation.prompt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.generation.pipeline.model.GenerationParams;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlide;
import ru.pp.gamma.overlord.presentation.template.service.TemplatePresentationService;

import java.util.ArrayList;
import java.util.List;

import static ru.pp.gamma.overlord.presentation.template.common.KeyConsts.SLIDE_TYPE;

@RequiredArgsConstructor
@Component
public class SystemPromptProvider {

    private static final String TEMPLATE = """
            %s
            
            Ответ должен состоять только из одного валидного JSON-объекта. JSON должен быть полностью валидным и готовым к парсингу.
            Каждый элемент массива — отдельный слайд.
            
            Требования:
            - запрещен любой текст до или после JSON
            - запрещено оборачивать JSON в ``` или другие символы
            - слайдов должно быть РОВНО столько, сколько было указано ранее. (если не было указано, то игнорируй)
            - используй только типы слайдов, указанные ниже
            
            Формат вывода: { "name": "краткое название презы, о чем она (название будущего файла)", "slides": [ { "slideType": "...", ... }, ... ] }
            
            Типы слайдов: %s
            """;

    private final TemplatePresentationService templatePresentationService;
    private final ObjectMapper objectMapper;

    public String getPrompt(GenerationParams params) {
        TemplatePresentation templatePresentation = templatePresentationService
                .getById(params.templatePresentationId());

        List<SystemPromptSlideModel> promptSlidesList = new ArrayList<>();

        templatePresentation.getSlides()
                .forEach(slideInfo -> promptSlidesList.add(buildPromptSlideModel(slideInfo)));


        try {
            return TEMPLATE.formatted(
                    params.userPrompt(),
                    objectMapper.writeValueAsString(promptSlidesList)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private SystemPromptSlideModel buildPromptSlideModel(TemplateSlide slide) {
        SystemPromptSlideModel model = new SystemPromptSlideModel();
        model.setSlideType(slide.getType());
        model.setDescription(slide.getPrompt());

        ObjectNode fields = objectMapper.createObjectNode();
        slide.getFields()
                .forEach(fieldInfo -> fields.put(fieldInfo.getJsonKey(), fieldInfo.getPrompt()));

        model.setFields(fields);
        model.setExample(buildExample(slide));
        return model;
    }

    private JsonNode buildExample(TemplateSlide slide) {
        ObjectNode example = objectMapper.createObjectNode();
        example.put(SLIDE_TYPE, slide.getType().name());

        slide.getFields().forEach(fieldInfo -> example.put(fieldInfo.getJsonKey(), fieldInfo.getExample()));

        return example;
    }

}

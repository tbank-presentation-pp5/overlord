package ru.pp.gamma.overlord.generation.prompt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlide;

import java.util.ArrayList;
import java.util.List;

import static ru.pp.gamma.overlord.presentation.template.common.KeyConsts.SLIDE_TYPE;

@RequiredArgsConstructor
@Component
public class DescriptionSlidesBuilder {

    private final ObjectMapper objectMapper;

    public String getDescription(TemplatePresentation template) {
        List<DescriptionSlideModel> promptSlidesList = new ArrayList<>();

        template.getSlides()
                .forEach(slideInfo -> promptSlidesList.add(buildPromptSlideModel(slideInfo)));

        try {
            return objectMapper.writeValueAsString(promptSlidesList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private DescriptionSlideModel buildPromptSlideModel(TemplateSlide slide) {
        DescriptionSlideModel model = new DescriptionSlideModel();
        model.setSlideType(slide.getType());
        model.setDescription(slide.getPrompt());

        ObjectNode fields = objectMapper.createObjectNode();
        slide.getFields()
                .forEach(fieldInfo -> fields.put(fieldInfo.getSchemaKey(), fieldInfo.getDescription()));

        model.setFields(fields);
        model.setExample(buildExample(slide));
        return model;
    }

    private JsonNode buildExample(TemplateSlide slide) {
        ObjectNode example = objectMapper.createObjectNode();
        example.put(SLIDE_TYPE, slide.getType().name());

        slide.getFields().forEach(fieldInfo -> example.put(fieldInfo.getSchemaKey(), fieldInfo.getExample()));

        return example;
    }
}

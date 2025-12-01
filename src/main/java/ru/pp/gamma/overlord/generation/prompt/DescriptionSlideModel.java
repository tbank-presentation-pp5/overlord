package ru.pp.gamma.overlord.generation.prompt;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentation.template.entity.SlideType;

@Getter
@Setter
public class DescriptionSlideModel {
    private SlideType slideType;
    private String description;
    private JsonNode fields;
    private JsonNode example;
}

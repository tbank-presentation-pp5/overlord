package ru.pp.gamma.overlord.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.ai.controller.dto.AiModelDto;
import ru.pp.gamma.overlord.ai.model.AiModel;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/ai/models")
public class AiModelController {

    @GetMapping
    public List<AiModelDto> getAll() {
        return Arrays.stream(AiModel.values())
                .map(m -> new AiModelDto(
                        m.name(),
                        m.getModelId(),
                        m.getDisplayName(),
                        m.getApiStyle().name()
                ))
                .toList();
    }
}
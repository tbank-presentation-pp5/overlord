package ru.pp.gamma.overlord.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.ai.controller.dto.AiImageModelDto;
import ru.pp.gamma.overlord.ai.model.AiImageModel;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/ai/models/image")
public class AiImageModelController {

    @GetMapping
    public List<AiImageModelDto> getAll() {
        return Arrays.stream(AiImageModel.values())
                .map(m -> new AiImageModelDto(
                        m.name(),
                        m.getModelId(),
                        m.getDisplayName(),
                        m.getApiStyle().name()
                ))
                .toList();
    }
}
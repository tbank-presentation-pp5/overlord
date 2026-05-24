package ru.pp.gamma.overlord.ai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.pp.gamma.overlord.ai.controller.dto.AiImageGenerateRequest;
import ru.pp.gamma.overlord.ai.controller.dto.AiImageGenerateResponse;
import ru.pp.gamma.overlord.ai.controller.dto.AiImageModelDto;
import ru.pp.gamma.overlord.ai.model.AiImageModel;
import ru.pp.gamma.overlord.ai.service.AiImageGenerateService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/ai/models/image")
@RequiredArgsConstructor
public class AiImageModelController {

    private final AiImageGenerateService aiImageGenerateService;

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

    @PostMapping("/generate")
    public AiImageGenerateResponse generate(@Valid @RequestBody AiImageGenerateRequest request) {
        String url = aiImageGenerateService.generate(
                request.prompt(),
                request.model(),
                request.width(),
                request.height()
        );
        return new AiImageGenerateResponse(url);
    }
}

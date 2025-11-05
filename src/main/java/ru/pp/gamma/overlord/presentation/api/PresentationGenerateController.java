package ru.pp.gamma.overlord.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.presentation.dto.PresentationGenerationFromTextRequest;
import ru.pp.gamma.overlord.presentation.service.PresentationGenerationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/generate/text")
public class PresentationGenerateController {

    private final PresentationGenerationService presentationGenerationService;

    @PostMapping
    public Object generateFromText(@RequestBody PresentationGenerationFromTextRequest request) {
        return presentationGenerationService.generate(request);
    }

}
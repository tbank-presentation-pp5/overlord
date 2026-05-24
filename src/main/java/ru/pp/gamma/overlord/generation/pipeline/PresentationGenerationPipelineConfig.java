package ru.pp.gamma.overlord.generation.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.pp.gamma.overlord.ai.api.AiImageClient;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.generation.pipeline.step.GenerateImagesStep;
import ru.pp.gamma.overlord.generation.pipeline.step.ParseAiResponseStep;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.image.service.NsfwPlaceholderService;

@Configuration
public class PresentationGenerationPipelineConfig {

    @Order(1)
    @Bean
    public ParseAiResponseStep parseAiResponseStep(
            AiTextClient aiTextClient,
            ObjectMapper objectMapper
    ) {
        return new ParseAiResponseStep(
                aiTextClient,
                objectMapper
        );
    }

    @Order(2)
    @Bean
    public GenerateImagesStep generateImagesStep(
            AiImageClient aiImageClient,
            ImageService imageService,
            NsfwPlaceholderService nsfwPlaceholderService
    ) {
        return new GenerateImagesStep(
                aiImageClient,
                imageService,
                nsfwPlaceholderService
        );
    }

}

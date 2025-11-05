package ru.pp.gamma.overlord.generation.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.generation.pipeline.step.ParseAiResponseStep;
import ru.pp.gamma.overlord.generation.prompt.SystemPromptProvider;
import ru.pp.gamma.overlord.presentation.template.service.TemplatePresentationService;

@Configuration
public class PresentationGenerationPipelineConfig {

    @Order(1)
    @Bean
    public ParseAiResponseStep parseAiResponseStep(
            SystemPromptProvider systemPromptProvider,
            AiTextClient aiTextClient,
            ObjectMapper objectMapper,
            TemplatePresentationService templatePresentationService
    ) {
        return new ParseAiResponseStep(
                systemPromptProvider,
                aiTextClient,
                objectMapper,
                templatePresentationService
        );
    }

}

package ru.pp.gamma.overlord.ai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.pp.gamma.overlord.ai.api.AiTextClient;

@Configuration
public class AiClientSelectionConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "cloudflare.api-text-style", havingValue = "path", matchIfMissing = true)
    public AiTextClient primaryPathStyleAiTextClient(
            @Qualifier("pathStyleAiTextClient") AiTextClient client) {
        return client;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "cloudflare.api-text-style", havingValue = "responses")
    public AiTextClient primaryResponsesStyleAiTextClient(
            @Qualifier("responsesStyleAiTextClient") AiTextClient client) {
        return client;
    }
}
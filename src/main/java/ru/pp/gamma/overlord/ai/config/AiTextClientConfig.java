package ru.pp.gamma.overlord.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfPathStyleAiTextClient;
import ru.pp.gamma.overlord.ai.cf.text.CfProps;
import ru.pp.gamma.overlord.ai.cf.text.CfResponsesStyleAiTextClient;

@Configuration
public class AiTextClientConfig {

    @Bean
    public AiTextClient aiTextClient(RestClient aiRestClient, CfProps cfProps) {
        String apiStyle = cfProps.getApiTextStyle();

        if ("path".equalsIgnoreCase(apiStyle)) {
            return new CfPathStyleAiTextClient(aiRestClient, cfProps);
        } else {
            return new CfResponsesStyleAiTextClient(aiRestClient, cfProps);
        }
    }
}
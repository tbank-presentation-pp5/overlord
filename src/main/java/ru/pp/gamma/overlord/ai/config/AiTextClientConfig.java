package ru.pp.gamma.overlord.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.pp.gamma.overlord.ai.cf.text.CfProps;

@EnableConfigurationProperties(CfProps.class)
@Configuration
public class AiTextClientConfig {
}
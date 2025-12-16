package ru.pp.gamma.overlord.ai.account;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccounterProps.class)
public class AccounterConfig {
}
package ru.pp.gamma.overlord.ai.account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "accounter")
public class AccounterProps {
    private String url;
    private boolean enabled;
    private int timeoutMs;
    private int maxRetries;
}
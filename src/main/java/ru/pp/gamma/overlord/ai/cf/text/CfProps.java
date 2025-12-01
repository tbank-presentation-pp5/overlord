package ru.pp.gamma.overlord.ai.cf.text;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloudflare")
public class CfProps {
    private String idAccount;
    private String authToken;
    private String pathStyleModel;
    private String responsesStyleModel;
    private String imageBase64Model;
}

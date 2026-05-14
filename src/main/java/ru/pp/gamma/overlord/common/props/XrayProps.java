package ru.pp.gamma.overlord.common.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "xray")
public class XrayProps {
    private String host;
    private int port;
    private boolean enabled;
}

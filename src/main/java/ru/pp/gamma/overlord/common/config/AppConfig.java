package ru.pp.gamma.overlord.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import ru.pp.gamma.overlord.common.props.XrayProps;

@Configuration
@EnableConfigurationProperties(value = {XrayProps.class})
@EnableWebSocket
public class AppConfig {
}

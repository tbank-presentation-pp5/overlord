package ru.pp.gamma.overlord.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.common.props.XrayProps;

import static ru.pp.gamma.overlord.common.util.ProxyUtil.prepareRestClientWithProxy;

@Configuration
public class AiConfig {

    @Bean
    public RestClient aiRestClient(XrayProps xrayProps) {
        if (xrayProps.isEnabled()) {
            return prepareRestClientWithProxy(xrayProps.getHost(), xrayProps.getPort()).build();
        }

        return RestClient.create();
    }

}

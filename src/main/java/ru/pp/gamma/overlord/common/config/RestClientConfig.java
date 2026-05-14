package ru.pp.gamma.overlord.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.common.props.XrayProps;

import static ru.pp.gamma.overlord.common.util.ProxyUtil.prepareRestClientWithProxy;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient proxyRestClient(XrayProps props) {
        if (!props.isEnabled()) {
            log.warn("Proxy rest client created without proxy!");
            return RestClient.create();
        }

        return prepareRestClientWithProxy(props.getHost(), props.getPort()).build();

    }

}

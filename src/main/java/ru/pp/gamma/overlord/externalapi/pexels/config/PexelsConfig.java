package ru.pp.gamma.overlord.externalapi.pexels.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.common.props.XrayProps;

import static ru.pp.gamma.overlord.common.util.ProxyUtil.prepareRestClientWithProxy;

@Slf4j
@Configuration
public class PexelsConfig {

    @Value("${external-client.pexels.api-key}")
    private String pexelsApiKey;

    @Value("${external-client.pexels.base-url}")
    private String pexelsBaseUrl;

    @Bean
    public RestClient pexelsRestClient(XrayProps props) {
        if (!props.isEnabled()) {
            log.warn("Pexels requires enabled proxy, but xray disabled");
            return buildWithAuthHeader(RestClient.builder());
        }

        return buildWithAuthHeader(prepareRestClientWithProxy(props.getHost(), props.getPort()));
    }

    private RestClient buildWithAuthHeader(RestClient.Builder builder) {
        return builder.defaultHeader("Authorization", pexelsApiKey)
                .baseUrl(pexelsBaseUrl)
                .build();
    }
}

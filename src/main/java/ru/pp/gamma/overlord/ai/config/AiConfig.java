package ru.pp.gamma.overlord.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.common.props.XrayProps;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class AiConfig {

    @Bean
    public RestClient aiRestClient(XrayProps xrayProps) {
        if (xrayProps.isEnabled()) {
            return buildWithProxy(xrayProps.getHost(), xrayProps.getPort());
        }

        return RestClient.create();
    }

    private RestClient buildWithProxy(String host, int port) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        return RestClient
                .builder()
                .requestFactory(requestFactory)
                .build();
    }

}

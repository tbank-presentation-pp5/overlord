package ru.pp.gamma.overlord.export.pdfv2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PDFV2Config {

    @Bean
    public RestClient pdfv2RestClient() {
        return RestClient.create();
    }

}

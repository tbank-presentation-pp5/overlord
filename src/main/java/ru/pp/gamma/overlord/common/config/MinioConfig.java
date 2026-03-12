package ru.pp.gamma.overlord.common.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pp.gamma.overlord.image.props.MinioProps;

@EnableConfigurationProperties(MinioProps.class)
@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProps minioProps) {
        return MinioClient.builder()
                .credentials(minioProps.getUser(), minioProps.getPassword())
                .endpoint(minioProps.getConnectUrl())
                .build();
    }

}

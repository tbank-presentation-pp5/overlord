package ru.pp.gamma.overlord.image.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProps {

    private String user;
    private String password;
    private String bucket;
    private String connectUrl;
    private String publicUrl;

}

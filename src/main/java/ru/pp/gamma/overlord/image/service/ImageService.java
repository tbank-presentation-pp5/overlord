package ru.pp.gamma.overlord.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.image.props.MinioProps;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private static final String EXTENSION = "jpeg";
    private static final String MIME_TYPE = "image/jpeg";
    private static final String EXTENSION_PNG = "png";
    private static final String MIME_TYPE_PNG = "image/png";

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioRepository minioRepository;
    private final MinioProps minioProps;
    private final Environment environment;

    public String save(byte[] image) {
        String name = generateName(EXTENSION);
        minioRepository.put(bucket, name, MIME_TYPE, image);
        return name;
    }

    public String savePng(byte[] image) {
        String name = generateName(EXTENSION_PNG);
        minioRepository.put(bucket, name, MIME_TYPE_PNG, image);
        return name;
    }

    public String generateUrlByName(String imageName) {
        String protocol = isProd() ? "https://" : "http://";
        return protocol + minioProps.getBucket() + "." + minioProps.getPublicDomain() + "/" + imageName;
    }

    private boolean isProd() {
        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

    private String generateName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }
}

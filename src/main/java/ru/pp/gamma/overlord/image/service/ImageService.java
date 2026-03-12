package ru.pp.gamma.overlord.image.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.image.enums.ImageFormat;
import ru.pp.gamma.overlord.image.props.MinioProps;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final MinioRepository minioRepository;
    private final MinioProps minioProps;
    private final Environment environment;

    private String baseImageUrl;

    @PostConstruct
    private void init() {
        String scheme = Arrays.asList(environment.getActiveProfiles()).contains("prod")
                ? "https" : "http";

        if (Boolean.TRUE.equals(minioProps.getUsePathStyle())) {
            baseImageUrl = scheme + "://" + minioProps.getPublicDomain() + "/" + minioProps.getBucket() + "/";
        } else {
            baseImageUrl = scheme + "://" + minioProps.getBucket() + "." + minioProps.getPublicDomain() + "/";
        }
    }

    public String save(byte[] image, ImageFormat format) {
        String name = UUID.randomUUID() + "." + format.getExtension();
        minioRepository.put(minioProps.getBucket(), name, format.getMimeType(), image);
        return name;
    }

    public String generateUrlByName(String imageName) {
        return baseImageUrl + imageName;
    }
}

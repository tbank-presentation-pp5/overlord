package ru.pp.gamma.overlord.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.image.props.MinioProps;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private static final String EXTENSION = "jpeg";
    private static final String MIME_TYPE = "image/jpeg";

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioRepository minioRepository;
    private final MinioProps minioProps;

    public String save(byte[] image) {
        String name = generateName();
        minioRepository.put(bucket, name, MIME_TYPE, image);
        return name;
    }

    public String generateUrlByName(String imageName) {
        return minioProps.getPublicUrl() + "/" + minioProps.getBucket() + "/" + imageName;
    }


    private String generateName() {
        return UUID.randomUUID().toString() + "." + EXTENSION;
    }
}

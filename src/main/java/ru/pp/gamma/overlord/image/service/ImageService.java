package ru.pp.gamma.overlord.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.image.props.MinioProps;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private static final String EXTENSION = "jpeg";
    private static final String MIME_TYPE = "image/jpeg";

    private final ImagesMinioService imagesMinioService;
    private final MinioProps minioProps;

    public String save(byte[] image) {
        String name = generateName();
        imagesMinioService.put(name, MIME_TYPE, image);
        return name;
    }

    public String generateUrlByName(String imageName) {
        return minioProps.getPublicUrl() + "/" + minioProps.getBucket() + "/" + imageName;
    }


    private String generateName() {
        return UUID.randomUUID().toString() + "." + EXTENSION;
    }
}

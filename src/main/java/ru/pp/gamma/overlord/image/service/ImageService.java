package ru.pp.gamma.overlord.image.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.enums.ImageFormat;
import ru.pp.gamma.overlord.image.props.MinioProps;
import ru.pp.gamma.overlord.image.repository.ImageRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private static final Set<ImageFormat> UPLOAD_ALLOWED_FORMATS = Set.of(ImageFormat.JPEG);

    private final MinioRepository minioRepository;
    private final MinioProps minioProps;
    private final Environment environment;
    private final ImageRepository imageRepository;

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
        String name = generateName(format);
        minioRepository.put(minioProps.getBucket(), name, format.getMimeType(), image);
        return name;
    }

    public String generateUrlByName(String imageName) {
        return baseImageUrl + imageName;
    }

    public Image uploadImage(MultipartFile image) {
        ImageFormat imageFormat = Optional.of(image)
                .map(MultipartFile::getContentType)
                .flatMap(ImageFormat::fromMimeType)
                .orElse(null);
        // Проверка не надежная, так как ориентируется на content type, который передает клиент, его можно подделать
        // Для точной проверки можно использовать apache tika
        if (imageFormat == null) {
            throw new RuntimeException("Image not valid, contentType=%s".formatted(image.getContentType()));
        }

        String name = generateName(imageFormat);
        Image imageEntity = new Image().setName(name);
        imageRepository.save(imageEntity);

        try {
            minioRepository.put(minioProps.getBucket(), name, imageFormat.getMimeType(), image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageEntity;
    }

    public Image getById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public void markAsDeleted(Image image) {
        image.setIsDeleted(true);
        imageRepository.save(image);
    }

    private String generateName(ImageFormat imageFormat) {
        return UUID.randomUUID() + "." + imageFormat.getExtension();
    }
}

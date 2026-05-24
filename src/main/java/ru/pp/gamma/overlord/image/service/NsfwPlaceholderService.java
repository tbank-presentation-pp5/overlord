package ru.pp.gamma.overlord.image.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.props.MinioProps;
import ru.pp.gamma.overlord.image.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class NsfwPlaceholderService {

    public static final String PLACEHOLDER_NAME = "nsfw-placeholder.jpeg";

    private final MinioRepository minioRepository;
    private final MinioProps minioProps;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    private Long placeholderImageId;

    @PostConstruct
    void ensurePlaceholderExists() {
        byte[] bytes = loadFromClasspathOrGenerateWhite();
        minioRepository.put(minioProps.getBucket(), PLACEHOLDER_NAME, "image/jpeg", bytes);
        log.info("NSFW placeholder uploaded to '{}/{}'.", minioProps.getBucket(), PLACEHOLDER_NAME);

        placeholderImageId = imageRepository.findByName(PLACEHOLDER_NAME)
                .orElseGet(() -> {
                    Image entity = new Image();
                    entity.setName(PLACEHOLDER_NAME);
                    return imageRepository.save(entity);
                }).getId();

        log.info("NSFW placeholder ready: id={}, url={}", placeholderImageId, getUrl());
    }

    public Image getPlaceholderImage() {
        return imageRepository.findById(placeholderImageId)
                .orElseThrow(() -> new RuntimeException("NSFW placeholder image not found in DB, id=" + placeholderImageId));
    }

    public String getUrl() {
        return imageService.generateUrlByName(PLACEHOLDER_NAME);
    }

    private byte[] loadFromClasspathOrGenerateWhite() {
        try (InputStream stream = getClass().getResourceAsStream("/nsfw-placeholder.jpeg")) {
            if (stream != null) {
                log.info("Loading NSFW placeholder from classpath.");
                return stream.readAllBytes();
            }
        } catch (IOException ignored) {
        }
        log.warn("nsfw-placeholder.jpeg not found in classpath, generating white image fallback.");
        return generateWhiteImage();
    }

    private byte[] generateWhiteImage() {
        try {
            BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
            int[] pixels = new int[512 * 512];
            Arrays.fill(pixels, 0xFFFFFF);
            img.setRGB(0, 0, 512, 512, pixels, 0, 512);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpeg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate placeholder image", e);
        }
    }
}

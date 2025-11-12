package ru.pp.gamma.overlord.image.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.image.dto.ImageResponse;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.service.ImageService;

@RequiredArgsConstructor
@Component
public class ImageMapper {

    private final ImageService imageService;

    public ImageResponse toImageResponse(Image image) {
        return new ImageResponse(
                image.getId(),
                imageService.generateUrlByName(image.getName())
        );
    }

}

package ru.pp.gamma.overlord.image.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pp.gamma.overlord.image.dto.ImageResponse;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.mapper.ImageMapper;
import ru.pp.gamma.overlord.image.service.ImageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageResponse uploadFile(@RequestParam("image") MultipartFile image) {
        Image uploadImage = imageService.uploadImage(image);
        return imageMapper.toImageResponse(uploadImage);
    }

}

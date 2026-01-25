package ru.pp.gamma.overlord.presentation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.image.dto.ImageResponse;
import ru.pp.gamma.overlord.image.mapper.ImageMapper;
import ru.pp.gamma.overlord.presentation.dto.PresentationResponse;
import ru.pp.gamma.overlord.presentation.dto.PresentationSlideResponse;
import ru.pp.gamma.overlord.presentation.dto.SlideContentResponse;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;

@RequiredArgsConstructor
@Component
public class ApiPresentationMapper {

    private final ImageMapper imageMapper;

    public PresentationResponse toResponse(Presentation presentation) {
        return new PresentationResponse(
                presentation.getTemplate().getId(),
                presentation.getId(),
                presentation.getName(),
                presentation.getCreatedAt(),
                presentation.getSlides().stream().map(this::toResponse).toList()
        );
    }

    private PresentationSlideResponse toResponse(PresentationSlide slide) {
        return new PresentationSlideResponse(
                slide.getId(),
                slide.getTemplate().getId(),
                slide.getTemplate().getType(),
                slide.getOrderNumber(),
                slide.getTemplate().isNeedPageNumber(),
                slide.getFields().stream().map(this::toResponse).toList()
        );
    }

    private SlideContentResponse toResponse(SlideField field) {
        return new SlideContentResponse(
                field.getTemplate().getId(),
                field.getId(),
                field.getTemplate().getContentType(),
                field.getTemplate().getSchemaKey(),
                field.getValue(),
                mapToImageIfNotNull(field)
        );
    }

    private ImageResponse mapToImageIfNotNull(SlideField field) {
        if (field.getImage() == null) {
            return null;
        }

        return imageMapper.toImageResponse(field.getImage());
    }

}

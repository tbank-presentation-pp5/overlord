package ru.pp.gamma.overlord.presentationpreview.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.image.mapper.ImageMapper;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentationpreview.dto.PresentationPreviewResponseDto;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class PresentationPreviewMapper {

    private final ImageMapper imageMapper;

    public List<PresentationPreviewResponseDto> toListDto(List<Presentation> presentations) {
        return presentations.stream()
                .map(this::toDto)
                .toList();
    }

    private PresentationPreviewResponseDto toDto(Presentation presentation) {
        return PresentationPreviewResponseDto.builder()
                .presentationId(presentation.getId())
                .name(presentation.getName())
                .updatedAt(presentation.getUpdatedAt())
                .previewUrls(buildPreviewUrls(presentation))
                .build();
    }

    private List<String> buildPreviewUrls(Presentation presentation) {
        return presentation.getSlides().stream()
                .map(PresentationSlide::getPreview)
                .filter(Objects::nonNull)
                .map(imageMapper::toImageUrl)
                .toList();
    }

}

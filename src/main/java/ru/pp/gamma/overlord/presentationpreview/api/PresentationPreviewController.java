package ru.pp.gamma.overlord.presentationpreview.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.common.util.PagingResponseDto;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.service.PresentationService;
import ru.pp.gamma.overlord.presentationpreview.dto.PresentationPreviewResponseDto;
import ru.pp.gamma.overlord.presentationpreview.mapper.PresentationPreviewMapper;

@RequiredArgsConstructor
@RequestMapping("/api/v1/presentations/previews")
@RestController
public class PresentationPreviewController {

    private final PresentationService presentationService;
    private final PresentationPreviewMapper presentationPreviewMapper;

    @GetMapping
    public PagingResponseDto<PresentationPreviewResponseDto> getPagingPreviews(
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Presentation> presentationPage = presentationService.getPageable(pageRequest);
        return new PagingResponseDto<>(
                presentationPreviewMapper.toListDto(presentationPage.getContent()),
                presentationPage.getTotalPages()
        );
    }

}


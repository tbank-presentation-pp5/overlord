package ru.pp.gamma.overlord.externalapi.pexels.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.common.util.PagingResponseDto;
import ru.pp.gamma.overlord.externalapi.pexels.client.PexelsClient;
import ru.pp.gamma.overlord.externalapi.pexels.dto.PexelsPhotoResponse;

@Tag(name = "external-clients", description = "Внешние интеграции, работающие по принципу \"проксирования запроса\"")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/external-clients/pexels")
public class PexelsApi {

    private final PexelsClient pexelsClient;

    @GetMapping("/image")
    public PagingResponseDto<PexelsPhotoResponse> searchImage(
            @RequestParam("query") @NotNull String query,
            @RequestParam("page") @Min(1) int page,
            @RequestParam("limit") @Min(1) @Max(80) int limit
    ) {
        var photos = pexelsClient.search(query, page, limit);
        return new PagingResponseDto<>(
                photos.data().stream()
                        .map(photo -> PexelsPhotoResponse.builder()
                                .id(photo.id())
                                .width(photo.width())
                                .height(photo.height())
                                .url(photo.src().large2x())
                                .photographer(photo.photographer())
                                .photographerUrl(photo.photographerUrl())
                                .alt(photo.alt())
                                .build())
                        .toList(),
                photos.totalResults()
        );
    }

}

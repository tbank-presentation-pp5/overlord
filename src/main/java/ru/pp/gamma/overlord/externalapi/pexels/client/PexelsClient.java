package ru.pp.gamma.overlord.externalapi.pexels.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.externalapi.pexels.dto.PexelsPagination;
import ru.pp.gamma.overlord.externalapi.pexels.dto.PexelsPhoto;

@RequiredArgsConstructor
@Component
public class PexelsClient {

    private static final String SEARCH = "/search";

    private final RestClient pexelsRestClient;

    /**
     * Поиск фотграфий по запросу
     *
     * @param query   текстовый запрос
     * @param page    номер страницы, начиная с 1
     * @param perPage сколько вернуть фотографий, максимум 80
     * @return фото с информацией
     */
    public PexelsPagination<PexelsPhoto> search(String query, int page, int perPage) {
        return pexelsRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEARCH)
                        .queryParam("query", query)
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<PexelsPagination<PexelsPhoto>>() {
                });
    }

}

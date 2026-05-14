package ru.pp.gamma.overlord.externalapi.pexels.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PexelsPagination<T>(
        @JsonProperty("total_results") int totalResults,
        int page,
        @JsonProperty("per_page") int perPage,
        @JsonAlias("photos") List<T> data,
        @JsonProperty("next_page") String nextPage
) {
}

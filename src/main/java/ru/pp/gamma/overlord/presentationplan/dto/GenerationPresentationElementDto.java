package ru.pp.gamma.overlord.presentationplan.dto;

import java.util.List;

public record GenerationPresentationElementDto(
        String title,
        List<String> points
) {
}

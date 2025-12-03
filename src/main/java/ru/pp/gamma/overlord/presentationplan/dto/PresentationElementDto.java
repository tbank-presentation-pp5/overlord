package ru.pp.gamma.overlord.presentationplan.dto;

import java.util.List;

public record PresentationElementDto(
        String title,
        List<String> points
) {
}

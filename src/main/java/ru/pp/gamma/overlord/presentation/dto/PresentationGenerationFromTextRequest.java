package ru.pp.gamma.overlord.presentation.dto;

public record PresentationGenerationFromTextRequest(String note, long templatePresentationId, int numberOfSlides) {
}

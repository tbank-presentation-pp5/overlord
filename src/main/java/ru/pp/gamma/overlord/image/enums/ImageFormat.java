package ru.pp.gamma.overlord.image.enums;

import lombok.Getter;

@Getter
public enum ImageFormat {
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png");

    private final String extension;
    private final String mimeType;

    ImageFormat(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }
}
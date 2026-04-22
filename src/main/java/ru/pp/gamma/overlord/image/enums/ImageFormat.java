package ru.pp.gamma.overlord.image.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<ImageFormat> fromMimeType(String mimeType) {
        return Arrays.stream(ImageFormat.values())
                .filter(format -> format.mimeType.equals(mimeType))
                .findFirst();
    }
}
package ru.pp.gamma.overlord.common.util;

public class CustomMinioException extends RuntimeException {
    public CustomMinioException(String message) {
        super(message);
    }

    public CustomMinioException(Throwable cause) {
        super(cause);
    }
}

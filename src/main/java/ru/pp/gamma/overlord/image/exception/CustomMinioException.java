package ru.pp.gamma.overlord.image.exception;

public class CustomMinioException extends RuntimeException {
    public CustomMinioException(String message) {
        super(message);
    }

    public CustomMinioException(Throwable cause) {
        super(cause);
    }
}

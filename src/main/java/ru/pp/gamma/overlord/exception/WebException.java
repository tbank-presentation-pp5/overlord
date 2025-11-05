package ru.pp.gamma.overlord.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WebException extends RuntimeException {

    private final HttpStatus status;

    public WebException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public WebException(HttpStatus status, String format, Object... args) {
        super(String.format(format, args));
        this.status = status;
    }
}

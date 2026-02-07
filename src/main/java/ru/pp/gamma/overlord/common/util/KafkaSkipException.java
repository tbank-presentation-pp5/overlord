package ru.pp.gamma.overlord.common.util;

public class KafkaSkipException extends RuntimeException {
    public KafkaSkipException(String message) {
        super(message);
    }
}

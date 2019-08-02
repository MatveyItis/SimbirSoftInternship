package ru.itis.maletskov.internship.util.exception;

public class YBotException extends RuntimeException {
    public YBotException(String message) {
        super(message);
    }

    public YBotException(String message, Throwable cause) {
        super(message, cause);
    }
}

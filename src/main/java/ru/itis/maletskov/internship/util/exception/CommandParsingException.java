package ru.itis.maletskov.internship.util.exception;

public class CommandParsingException extends Exception {
    public CommandParsingException(String message) {
        super(message);
    }

    public CommandParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

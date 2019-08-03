package ru.itis.maletskov.internship.util.exception;

public class ChatException extends Exception {
    public ChatException(String message) {
        super(message);
    }

    public ChatException(String message, Throwable t) {
        super(message, t);
    }
}

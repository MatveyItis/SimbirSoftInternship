package ru.itis.maletskov.internship.util.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}

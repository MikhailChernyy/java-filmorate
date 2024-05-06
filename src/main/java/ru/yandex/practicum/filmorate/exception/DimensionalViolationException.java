package ru.yandex.practicum.filmorate.exception;

public class DimensionalViolationException extends RuntimeException {
    public DimensionalViolationException(String message) {
        super(message);
    }
}

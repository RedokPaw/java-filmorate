package ru.yandex.practicum.filmorate.exceptions;

public class DeleteException extends RuntimeException {
    public DeleteException(String message) {
        super(message);
    }
}
